package com.xumic.solos_hud

import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.view.KeyEvent
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {

    private var intentChannel: MethodChannel? = null

    // Holds an intent received before Flutter engine was ready
    private var pendingIntentData: Map<String, Any?>? = null

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        // ── RFCOMM Bluetooth plugin ────────────────────────────────────────────
        val plugin = SolosBluetoothPlugin()
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            SolosBluetoothPlugin.METHOD_CHANNEL,
        ).setMethodCallHandler(plugin)
        EventChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            SolosBluetoothPlugin.EVENT_CHANNEL,
        ).setStreamHandler(plugin)

        // ── Foreground service ─────────────────────────────────────────────────
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            "solos_hud_service",
        ).setMethodCallHandler { call, result ->
            when (call.method) {
                "startForeground" -> {
                    val title = call.argument<String>("title")
                    val text  = call.argument<String>("text")
                    val svc = Intent(this, HudForegroundService::class.java).apply {
                        action = HudForegroundService.ACTION_START
                        if (title != null) putExtra(HudForegroundService.EXTRA_TITLE, title)
                        if (text  != null) putExtra(HudForegroundService.EXTRA_TEXT,  text)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(svc)
                    else startService(svc)
                    result.success(null)
                }
                "updateForeground" -> {
                    val title = call.argument<String>("title")
                    val text  = call.argument<String>("text")
                    val svc = Intent(this, HudForegroundService::class.java).apply {
                        action = HudForegroundService.ACTION_UPDATE
                        if (title != null) putExtra(HudForegroundService.EXTRA_TITLE, title)
                        if (text  != null) putExtra(HudForegroundService.EXTRA_TEXT,  text)
                    }
                    startService(svc)
                    result.success(null)
                }
                "stopForeground" -> {
                    val svc = Intent(this, HudForegroundService::class.java).apply {
                        action = HudForegroundService.ACTION_STOP
                    }
                    startService(svc)
                    result.success(null)
                }
                else -> result.notImplemented()
            }
        }

        // ── Notification listener EventChannel ────────────────────────────────
        EventChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            SolosNotificationService.EVENT_CHANNEL,
        ).setStreamHandler(object : EventChannel.StreamHandler {
            override fun onListen(arguments: Any?, events: EventChannel.EventSink) {
                SolosNotificationService.eventSink = events
            }
            override fun onCancel(arguments: Any?) {
                SolosNotificationService.eventSink = null
            }
        })

        // ── Intent / Share channel ─────────────────────────────────────────────
        intentChannel = MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            "solos_intent",
        )
        intentChannel!!.setMethodCallHandler { call, result ->
            when (call.method) {
                // Dart calls this on startup to retrieve intent data that arrived
                // before the engine was ready (i.e. a cold-start share).
                "getInitialIntent" -> {
                    result.success(pendingIntentData)
                    pendingIntentData = null
                }
                // Returns true if any audio is currently being played
                "isMusicPlaying" -> {
                    val audio = getSystemService(AUDIO_SERVICE) as AudioManager
                    result.success(audio.isMusicActive)
                }
                // Media controls via AudioManager key events
                "mediaPlayPause" -> {
                    dispatchMediaKey(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
                    result.success(null)
                }
                "mediaNext" -> {
                    dispatchMediaKey(KeyEvent.KEYCODE_MEDIA_NEXT)
                    result.success(null)
                }
                "mediaPrev" -> {
                    dispatchMediaKey(KeyEvent.KEYCODE_MEDIA_PREVIOUS)
                    result.success(null)
                }
                "volumeUp" -> {
                    adjustVolume(AudioManager.ADJUST_RAISE)
                    result.success(null)
                }
                "volumeDown" -> {
                    adjustVolume(AudioManager.ADJUST_LOWER)
                    result.success(null)
                }
                // Open the system Notification Access settings page
                "openNotificationSettings" -> {
                    val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                    startActivity(intent)
                    result.success(null)
                }
                // Open a URL/URI in the appropriate app
                "openUrl" -> {
                    val url = call.argument<String>("url") ?: return@setMethodCallHandler
                    try {
                        val uri = android.net.Uri.parse(url)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        result.success(null)
                    } catch (e: Exception) {
                        result.error("OPEN_URL_FAILED", e.message, null)
                    }
                }
                // Launch the phone's built-in voice assistant.
                // target: "google_assistant" | "bixby" | "voice_assist" (default)
                "launchAssistant" -> {
                    val target = call.argument<String>("target") ?: "voice_assist"
                    try {
                        val intent: Intent = when (target) {
                            "google_assistant" ->
                                packageManager.getLaunchIntentForPackage(
                                    "com.google.android.apps.googleassistant"
                                ) ?: Intent("android.intent.action.VOICE_ASSIST")
                            "bixby" ->
                                packageManager.getLaunchIntentForPackage(
                                    "com.samsung.android.bixby.agent"
                                ) ?: Intent("android.intent.action.VOICE_ASSIST")
                            else -> // "voice_assist" — triggers whichever voice assistant the user has set
                                Intent("android.intent.action.VOICE_ASSIST")
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        result.success(null)
                    } catch (e: Exception) {
                        // Final fallback: generic ACTION_ASSIST
                        try {
                            startActivity(Intent(Intent.ACTION_ASSIST)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                            result.success(null)
                        } catch (e2: Exception) {
                            result.error("LAUNCH_ASSISTANT_FAILED", e2.message, null)
                        }
                    }
                }
                else -> result.notImplemented()
            }
        }

        // ── Notification action invoker ────────────────────────────────────────
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            "solos_actions",
        ).setMethodCallHandler { call, result ->
            when (call.method) {
                "invoke" -> {
                    val key   = call.argument<String>("key") ?: ""
                    val index = call.argument<Int>("index") ?: 0
                    val reply = call.argument<String>("reply")
                    val ok = SolosNotificationService.fireAction(applicationContext, key, index, reply)
                    result.success(ok)
                }
                else -> result.notImplemented()
            }
        }

        // Parse the launch intent
        pendingIntentData = parseIntent(intent)
    }

    // Called when app is already running and a new intent arrives
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val data = parseIntent(intent) ?: return
        // If the engine is up, push directly; otherwise store for getInitialIntent
        intentChannel?.invokeMethod("onSharedIntent", data)
            ?: run { pendingIntentData = data }
    }

    private fun dispatchMediaKey(keyCode: Int) {
        val audio = getSystemService(AUDIO_SERVICE) as AudioManager
        val down = KeyEvent(KeyEvent.ACTION_DOWN, keyCode)
        val up   = KeyEvent(KeyEvent.ACTION_UP,   keyCode)
        audio.dispatchMediaKeyEvent(down)
        audio.dispatchMediaKeyEvent(up)
    }

    private fun adjustVolume(direction: Int) {
        val audio = getSystemService(AUDIO_SERVICE) as AudioManager
        audio.adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            direction,
            AudioManager.FLAG_SHOW_UI
        )
    }

    private fun parseIntent(intent: Intent?): Map<String, Any?>? {
        if (intent == null) return null
        return when (intent.action) {
            Intent.ACTION_SEND -> {
                if (intent.type != "text/plain") return null
                val text = intent.getStringExtra(Intent.EXTRA_TEXT)?.trim() ?: return null
                mapOf("type" to "share_text", "text" to text)
            }
            Intent.ACTION_VIEW -> {
                val uri = intent.data ?: return null
                val raw = uri.toString()
                when {
                    uri.scheme == "geo" -> mapOf("type" to "geo_uri", "uri" to raw)
                    raw.contains("maps.app.goo.gl") ||
                    raw.contains("maps.google.com") ||
                    raw.contains("goo.gl/maps") ->
                        mapOf("type" to "maps_url", "url" to raw)
                    else -> null
                }
            }
            else -> null
        }
    }
}
