package com.mighty.sitcomwidget

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder

class SitcomKeepAliveService : Service() {

    override fun onCreate() {
        super.onCreate()
        SoundPlayer.init(this)
        startInForeground()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        SoundPlayer.init(this)
        if (intent?.action == SitcomWidgetProvider.ACTION_PLAY_LAUGH) {
            SoundPlayer.play(this)
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startInForeground() {
        val channelId = "sitcom_keep_alive_channel"
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Sitcom Laughs Instant Engine",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Mantiene las risas pre-cargadas en RAM para sonar sin demora"
            setShowBadge(false)
        }
        manager.createNotificationChannel(channel)

        val playIntent = Intent(this, SitcomWidgetProvider::class.java).apply {
            action = SitcomWidgetProvider.ACTION_PLAY_LAUGH
        }
        val playPendingIntent = PendingIntent.getBroadcast(
            this,
            201,
            playIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = Notification.Builder(this, channelId)
            .setContentTitle("😂 Risas listas al instante")
            .setContentText("Motor de audio activo en RAM • Toca para sonar")
            .setSmallIcon(android.R.drawable.ic_lock_silent_mode_off)
            .setContentIntent(playPendingIntent)
            .setOngoing(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                1001,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
        } else {
            startForeground(1001, notification)
        }
    }

    companion object {
        fun ensureRunning(context: Context) {
            try {
                val intent = Intent(context.applicationContext, SitcomKeepAliveService::class.java)
                context.applicationContext.startForegroundService(intent)
            } catch (_: Exception) {
                // Ignore background start restrictions if triggered while restricted
            }
        }
    }
}
