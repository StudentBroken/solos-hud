package com.xumic.solos_hud

import android.app.Notification
import android.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import io.flutter.plugin.common.EventChannel
import java.io.ByteArrayOutputStream
import java.util.concurrent.ConcurrentHashMap

class SolosNotificationService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val sink = eventSink ?: return

        val isOngoing = (sbn.notification.flags and Notification.FLAG_ONGOING_EVENT) != 0
        val isCall = sbn.packageName in CALL_PACKAGES ||
                sbn.notification.category == Notification.CATEGORY_CALL

        // Allow calls through even if ongoing
        if (isOngoing && !isCall
                && sbn.packageName !in NAVIGATION_PACKAGES
                && sbn.packageName !in MEDIA_PACKAGES) return

        if (sbn.packageName in BLOCKLIST) return

        val extras  = sbn.notification.extras
        val title   = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString()    ?: ""
        val text    = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()     ?: ""
        val bigText = extras.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString() ?: text
        val artist  = extras.getCharSequence(Notification.EXTRA_SUB_TEXT)?.toString() ?: ""

        val isMessage = sbn.packageName in MESSAGE_PACKAGES ||
                sbn.notification.category == Notification.CATEGORY_MESSAGE ||
                sbn.notification.category == Notification.CATEGORY_EMAIL ||
                sbn.notification.category == Notification.CATEGORY_SOCIAL

        val pm = applicationContext.packageManager
        val appName = try {
            pm.getApplicationLabel(pm.getApplicationInfo(sbn.packageName, 0)).toString()
        } catch (_: Exception) { sbn.packageName }

        // Extract actions
        val rawActions = sbn.notification.actions ?: emptyArray()
        val actionsList = mutableListOf<Map<String, Any?>>()
        if (rawActions.isNotEmpty()) {
            storedActions[sbn.key] = rawActions
            rawActions.forEachIndexed { index, action ->
                val hasReply = action.remoteInputs?.any { ri ->
                    ri.allowFreeFormInput
                } == true
                actionsList.add(mapOf(
                    "index"    to index,
                    "label"    to (action.title?.toString() ?: ""),
                    "hasReply" to hasReply,
                ))
            }
        }

        val data = hashMapOf<String, Any?>(
            "packageName" to sbn.packageName,
            "appName"     to appName,
            "title"       to title,
            "text"        to text,
            "bigText"     to bigText,
            "artist"      to artist,
            "timestamp"   to sbn.postTime,
            "key"         to sbn.key,
            "isOngoing"   to isOngoing,
            "isMedia"     to (sbn.packageName in MEDIA_PACKAGES),
            "isCall"      to isCall,
            "isMessage"   to isMessage,
            "actions"     to actionsList,
        )

        // Extract album art for media notifications
        if (sbn.packageName in MEDIA_PACKAGES) {
            val albumArt = extractAlbumArt(sbn)
            if (albumArt != null) data["albumArt"] = albumArt
        }

        android.os.Handler(android.os.Looper.getMainLooper()).post {
            sink.success(data)
        }
    }

    private fun extractAlbumArt(sbn: StatusBarNotification): ByteArray? {
        return try {
            val extras = sbn.notification.extras

            // Try EXTRA_PICTURE first (highest quality).
            // Android 13+ (API 33) deprecated the non-typed getParcelable — use typed variant.
            val picture: Bitmap? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                extras.getParcelable(Notification.EXTRA_PICTURE, Bitmap::class.java)
            } else {
                @Suppress("DEPRECATION") extras.getParcelable(Notification.EXTRA_PICTURE)
            }
            if (picture != null) return bitmapToJpeg(picture, 90)

            // Try large icon (most reliable source for album art on API 23+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val largeIcon = sbn.notification.getLargeIcon()
                val drawable  = largeIcon?.loadDrawable(applicationContext)
                if (drawable is BitmapDrawable && drawable.bitmap != null) {
                    return bitmapToJpeg(drawable.bitmap, 85)
                }
            }

            // Fallback: EXTRA_LARGE_ICON bitmap (pre-M or fallback)
            val bmp: Bitmap? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                extras.getParcelable(Notification.EXTRA_LARGE_ICON, Bitmap::class.java)
            } else {
                @Suppress("DEPRECATION") extras.getParcelable(Notification.EXTRA_LARGE_ICON)
            }
            if (bmp != null) return bitmapToJpeg(bmp, 85)

            null
        } catch (_: Exception) { null }
    }

    private fun bitmapToJpeg(bmp: Bitmap, quality: Int): ByteArray {
        // Scale to 128×128 — enough detail for the glasses HUD
        val scaled = Bitmap.createScaledBitmap(bmp, 128, 128, true)
        val out    = ByteArrayOutputStream()
        scaled.compress(Bitmap.CompressFormat.JPEG, quality, out)
        return out.toByteArray()
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        storedActions.remove(sbn.key)
        val sink = eventSink ?: return
        android.os.Handler(android.os.Looper.getMainLooper()).post {
            sink.success(hashMapOf<String, Any?>(
                "removed"     to true,
                "key"         to sbn.key,
                "packageName" to sbn.packageName,
            ))
        }
    }

    companion object {
        const val EVENT_CHANNEL = "solos_notifications"

        @Volatile var eventSink: EventChannel.EventSink? = null

        // Stored actions keyed by notification sbn.key
        val storedActions = ConcurrentHashMap<String, Array<Notification.Action>>()

        val NAVIGATION_PACKAGES = setOf(
            "com.google.android.apps.maps",
            "com.waze",
        )

        val MEDIA_PACKAGES = setOf(
            "com.google.android.apps.youtube.music",
            "com.spotify.music",
            "com.apple.android.music",
            "com.amazon.mp3",
            "com.soundcloud.android",
            "com.tidal.music",
            "com.deezer.android",
            "com.pandora.android",
            "com.musicplayer.musicapps",
            "com.maxmpz.audioplayer",        // Poweramp
            "com.android.music",
        )

        val CALL_PACKAGES = setOf(
            "com.android.phone",
            "com.android.server.telecom",
            "com.google.android.dialer",
            "com.samsung.android.incallui",
        )

        val MESSAGE_PACKAGES = setOf(
            "com.whatsapp",
            "com.whatsapp.w4b",
            "org.thoughtcrime.securesms",    // Signal
            "com.facebook.orca",             // Messenger
            "com.instagram.android",
            "com.snapchat.android",
            "com.telegram.messenger",
            "org.telegram.messenger",
            "com.discord",
            "com.google.android.apps.messaging", // Google Messages
            "com.android.mms",
            "com.samsung.android.messaging",
        )

        val BLOCKLIST = setOf(
            "android",
            "com.android.systemui",
            "com.android.bluetooth",
        )

        /**
         * Fire a notification action by key + index.
         * If [replyText] is provided and the action has a RemoteInput,
         * it fills the reply before firing.
         * Returns true on success.
         */
        fun fireAction(context: Context, key: String, actionIndex: Int, replyText: String? = null): Boolean {
            return try {
                val actions = storedActions[key] ?: return false
                if (actionIndex < 0 || actionIndex >= actions.size) return false
                val action = actions[actionIndex]

                val fillIntent = Intent()
                if (replyText != null) {
                    val remoteInputs = action.remoteInputs
                    if (!remoteInputs.isNullOrEmpty()) {
                        val bundle = Bundle()
                        for (ri in remoteInputs) {
                            bundle.putCharSequence(ri.resultKey, replyText)
                        }
                        RemoteInput.addResultsToIntent(remoteInputs, fillIntent, bundle)
                    }
                }

                action.actionIntent.send(context, 0, fillIntent)
                true
            } catch (_: Exception) {
                false
            }
        }
    }
}
