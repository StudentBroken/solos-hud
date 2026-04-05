package com.xumic.solos_hud

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.os.Looper
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class SolosBluetoothPlugin : MethodChannel.MethodCallHandler, EventChannel.StreamHandler {

    companion object {
        const val METHOD_CHANNEL = "solos_rfcomm"
        const val EVENT_CHANNEL  = "solos_rfcomm_events"
        private const val SERVICE_UUID   = "000011A0-0000-1000-8000-00805F9B34FB"
        private const val MAGIC          = 0x601D
        private const val QUEUE_CAPACITY = 8
    }

    private val mainHandler = Handler(Looper.getMainLooper())

    // ── Connection state ──────────────────────────────────────────────────────
    // @Volatile ensures cross-thread visibility without full synchronisation.
    @Volatile private var socket:       BluetoothSocket? = null
    @Volatile private var outputStream: OutputStream?    = null
    @Volatile private var inputStream:  InputStream?     = null
    @Volatile private var eventSink:    EventChannel.EventSink? = null

    // Monotonically-increasing counter.  Every new connect bumps this.
    // Threads from previous connections compare their id to the current
    // value; if they differ, they silently exit without touching shared state.
    // This eliminates the race where an old write-thread's error handler
    // nulls out the *new* connection's socket.
    private val connectionGen = AtomicInteger(0)

    private var readThread:  Thread? = null
    private var writeThread: Thread? = null
    private val writeQueue  = LinkedBlockingQueue<Pair<ByteArray, MethodChannel.Result>>(QUEUE_CAPACITY)

    // Prevent concurrent connect attempts from racing each other.
    // If a connect is already in flight, new calls are rejected silently.
    @Volatile private var _connecting = false

    // Maximum safe packet size for the glasses' RFCOMM receive buffer.
    // JPEG-encoded HUD frames are typically 5–15 KB; allow up to 32 KB so
    // complex scenes don't get silently dropped (the original 10 KB limit was
    // overly conservative and caused blank frames for Dashboard/Workout apps).
    private val MAX_PACKET_BYTES = 32 * 1024 // 32 KB hard limit
    private val WRITE_CHUNK_BYTES = 512

    // ── MethodChannel ─────────────────────────────────────────────────────────

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "getPairedDevices" -> getPairedDevices(result)

            "connect" -> {
                val address = call.argument<String>("address")
                    ?: return result.error("INVALID", "No address", null)
                // Silently ignore duplicate connect calls while one is in flight.
                // The caller will get a result from the in-flight attempt.
                if (_connecting) {
                    mainHandler.post { result.success(false) }
                    return
                }
                Thread { connectInternal(address, result) }.start()
            }

            "disconnect" -> {
                _connecting = false
                Thread {
                    disconnectInternal(connectionGen.incrementAndGet())
                    mainHandler.post { result.success(null) }
                }.start()
            }

            "send" -> {
                val bytes = call.argument<ByteArray>("bytes")
                    ?: return result.error("INVALID", "No bytes", null)

                // Hard-drop frames that exceed the glasses' receive buffer.
                // A 17 KB packet overflows the ~16 KB RFCOMM buffer and causes
                // the glasses firmware to reset the connection.
                if (bytes.size > MAX_PACKET_BYTES) {
                    mainHandler.post { result.success(null) } // silently drop
                    return
                }

                if (socket?.isConnected != true) {
                    // Not connected — drop silently (don't error, avoids spurious disconnects)
                    mainHandler.post { result.success(null) }
                    return
                }
                // Drop oldest frame if queue is full
                if (writeQueue.size >= QUEUE_CAPACITY) writeQueue.poll()
                if (!writeQueue.offer(bytes to result)) {
                    mainHandler.post { result.success(null) }
                }
            }

            "isConnected" -> result.success(socket?.isConnected == true)

            else -> result.notImplemented()
        }
    }

    private fun getPairedDevices(result: MethodChannel.Result) {
        try {
            val bonded = BluetoothAdapter.getDefaultAdapter()?.bondedDevices ?: emptySet()
            result.success(bonded.map { mapOf("name" to (it.name ?: ""), "address" to it.address) })
        } catch (e: Exception) {
            result.error("FAILED", e.message, null)
        }
    }

    // ── Connect ───────────────────────────────────────────────────────────────

    private fun connectInternal(address: String, result: MethodChannel.Result) {
        _connecting = true
        val myGen = connectionGen.incrementAndGet()
        cleanupThreads()

        try {
            val adapter = BluetoothAdapter.getDefaultAdapter()
            adapter?.cancelDiscovery()

            val device = adapter?.getRemoteDevice(address)
                ?: throw IOException("Device not found: $address")

            val newSocket: BluetoothSocket = try {
                device.createRfcommSocketToServiceRecord(UUID.fromString(SERVICE_UUID))
            } catch (_: Exception) {
                @Suppress("DiscouragedPrivateApi")
                device.javaClass
                    .getMethod("createInsecureRfcommSocket", Int::class.java)
                    .invoke(device, 1) as BluetoothSocket
            }

            newSocket.connect()

            // Only assign to shared state if we're still the current generation.
            // A concurrent disconnect() would have bumped connectionGen again.
            if (connectionGen.get() != myGen) {
                try { newSocket.close() } catch (_: IOException) {}
                mainHandler.post { result.error("CONNECT_FAILED", "Superseded by newer connect/disconnect", null) }
                return
            }

            socket       = newSocket
            outputStream = newSocket.outputStream
            inputStream  = newSocket.inputStream

            startReadLoop(myGen)
            startWriteThread(myGen)

            _connecting = false
            mainHandler.post { result.success(true) }
        } catch (e: Exception) {
            _connecting = false
            mainHandler.post { result.error("CONNECT_FAILED", e.message, null) }
        }
    }

    // ── Disconnect ────────────────────────────────────────────────────────────

    private fun disconnectInternal(gen: Int = connectionGen.get()) {
        cleanupThreads()
        closeStreams()
    }

    private fun cleanupThreads() {
        writeThread?.interrupt()
        writeThread = null
        writeQueue.clear()
        readThread?.interrupt()
        readThread = null
    }

    private fun closeStreams() {
        try { outputStream?.close() } catch (_: IOException) {}
        try { inputStream?.close()  } catch (_: IOException) {}
        try { socket?.close()       } catch (_: IOException) {}
        socket       = null
        outputStream = null
        inputStream  = null
    }

    // ── Write thread (one per connection) ─────────────────────────────────────

    private fun startWriteThread(myGen: Int) {
        writeThread?.interrupt()
        writeThread = Thread {
            try {
                while (!Thread.currentThread().isInterrupted) {
                    val (bytes, result) = writeQueue.poll(2, TimeUnit.SECONDS) ?: continue
                    if (connectionGen.get() != myGen) {
                        // A new connection was established — discard this write
                        mainHandler.post { result.success(null) }
                        break
                    }
                    val out = outputStream
                    if (out == null) {
                        mainHandler.post { result.error("NOT_CONNECTED", "No output stream", null) }
                        break
                    }
                    try {
                        // Write in chunks so the Android BT stack doesn't
                        // choke on large single-call writes.
                        var offset = 0
                        while (offset < bytes.size) {
                            val end = minOf(offset + WRITE_CHUNK_BYTES, bytes.size)
                            out.write(bytes, offset, end - offset)
                            offset = end
                        }
                        out.flush()
                        mainHandler.post { result.success(null) }
                    } catch (e: Exception) {
                        writeQueue.clear()
                        // Only signal disconnection if we're still the live connection.
                        // If connectionGen has been bumped, a new connection is in progress
                        // and we must NOT touch socket/outputStream.
                        if (connectionGen.compareAndSet(myGen, myGen)) {
                            // Still the current connection — clean up
                            socket       = null
                            outputStream = null
                            mainHandler.post {
                                result.error("SEND_FAILED", e.message, null)
                            }
                        } else {
                            mainHandler.post { result.success(null) }
                        }
                        break
                    }
                }
            } catch (_: InterruptedException) {}
        }.also { it.isDaemon = true; it.name = "BT-Write-$myGen"; it.start() }
    }

    // ── Read loop (one per connection) ────────────────────────────────────────

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        eventSink = events
        if (socket?.isConnected == true && readThread == null) {
            startReadLoop(connectionGen.get())
        }
    }

    override fun onCancel(arguments: Any?) { eventSink = null }

    private fun startReadLoop(myGen: Int) {
        readThread?.interrupt()
        val stream = inputStream ?: return

        readThread = Thread {
            val headerBuf = ByteArray(8)
            try {
                while (!Thread.currentThread().isInterrupted
                       && socket?.isConnected == true
                       && connectionGen.get() == myGen) {

                    if (!findMagic(stream)) break

                    if (!readFully(stream, headerBuf, 8)) break
                    val type       = (headerBuf[0].toInt() and 0xFF) or
                                     ((headerBuf[1].toInt() and 0xFF) shl 8)
                    val payloadLen = (headerBuf[4].toInt() and 0xFF) or
                                     ((headerBuf[5].toInt() and 0xFF) shl 8) or
                                     ((headerBuf[6].toInt() and 0xFF) shl 16) or
                                     ((headerBuf[7].toInt() and 0xFF) shl 24)

                    if (payloadLen < 0 || payloadLen > 65536) continue
                    val payload = ByteArray(payloadLen)
                    if (payloadLen > 0 && !readFully(stream, payload, payloadLen)) break

                    if (connectionGen.get() != myGen) break // superseded

                    val sink = eventSink ?: continue
                    mainHandler.post {
                        sink.success(mapOf("type" to type, "payload" to payload))
                    }
                }
            } catch (_: IOException) {}

            // Signal disconnection only if we're still the live connection
            if (connectionGen.get() == myGen) {
                mainHandler.post { eventSink?.endOfStream() }
            }
        }.also { it.isDaemon = true; it.name = "BT-Read-$myGen"; it.start() }
    }

    // ── Packet parsing helpers ────────────────────────────────────────────────

    private fun findMagic(stream: InputStream): Boolean {
        var prev = 0
        while (true) {
            val b = stream.read()
            if (b < 0) return false
            if (((prev and 0xFF) or ((b and 0xFF) shl 8)) == MAGIC) return true
            prev = b
        }
    }

    private fun readFully(stream: InputStream, buf: ByteArray, len: Int): Boolean {
        var total = 0
        while (total < len) {
            val n = stream.read(buf, total, len - total)
            if (n < 0) return false
            total += n
        }
        return true
    }
}
