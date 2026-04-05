package com.xumic.solos_hud

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

/**
 * Foreground service that keeps the Flutter engine alive while the screen is off
 * or the app is backgrounded.
 *
 * The HUD loop, BT connection, GPS, and compass all run in the Flutter/Dart isolate.
 * This service just holds a startForeground() lock so Android cannot kill the process.
 *
 * Actions:
 *   ACTION_START  — start (or keep) the service, optional extras: EXTRA_TITLE, EXTRA_TEXT
 *   ACTION_UPDATE — update notification text without restarting, extras: EXTRA_TITLE, EXTRA_TEXT
 *   ACTION_STOP   — stop the service
 */
class HudForegroundService : Service() {

    companion object {
        const val CHANNEL_ID        = "solos_hud_channel"
        const val NOTIFICATION_ID   = 1001
        const val ACTION_START      = "com.xumic.solos_hud.START"
        const val ACTION_UPDATE     = "com.xumic.solos_hud.UPDATE"
        const val ACTION_STOP       = "com.xumic.solos_hud.STOP"
        const val EXTRA_TITLE       = "notif_title"
        const val EXTRA_TEXT        = "notif_text"

        private const val DEFAULT_TITLE = "Solos HUD"
        private const val DEFAULT_TEXT  = "Running in background"
    }

    private var currentTitle = DEFAULT_TITLE
    private var currentText  = DEFAULT_TEXT

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val title = intent?.getStringExtra(EXTRA_TITLE)
        val text  = intent?.getStringExtra(EXTRA_TEXT)
        if (title != null) currentTitle = title
        if (text  != null) currentText  = text

        return when (intent?.action) {
            ACTION_STOP -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
                START_NOT_STICKY
            }
            ACTION_UPDATE -> {
                // Refresh the notification in-place
                val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                nm.notify(NOTIFICATION_ID, buildNotification())
                START_STICKY
            }
            else -> {
                // ACTION_START or first launch
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    startForeground(
                        NOTIFICATION_ID,
                        buildNotification(),
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE or
                            ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION,
                    )
                } else {
                    startForeground(NOTIFICATION_ID, buildNotification())
                }
                START_STICKY
            }
        }
    }

    private fun buildNotification(): Notification {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(currentTitle)
            .setContentText(currentText)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setSilent(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Solos HUD",
                NotificationManager.IMPORTANCE_LOW,
            ).apply {
                description = "Keeps the Solos HUD active while screen is off"
                setShowBadge(false)
                enableLights(false)
                enableVibration(false)
            }
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }
}
