package com.mighty.sitcomwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class SitcomWidgetProvider : AppWidgetProvider() {

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        SoundPlayer.init(context)
        SitcomKeepAliveService.ensureRunning(context)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        SoundPlayer.init(context)
        SitcomKeepAliveService.ensureRunning(context)
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            ACTION_PLAY_LAUGH -> {
                SoundPlayer.play(context)
                SitcomKeepAliveService.ensureRunning(context)
            }
            Intent.ACTION_BOOT_COMPLETED -> {
                SoundPlayer.init(context)
                SitcomKeepAliveService.ensureRunning(context)
            }
        }
    }

    companion object {
        const val ACTION_PLAY_LAUGH = "com.mighty.sitcomwidget.ACTION_PLAY_LAUGH"

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_sitcom)
            val pendingIntent = buildBroadcastPendingIntent(context)

            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)
            views.setOnClickPendingIntent(R.id.btn_widget_laugh, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun buildBroadcastPendingIntent(context: Context): PendingIntent {
            val intent = Intent(context, SitcomWidgetProvider::class.java).apply {
                action = ACTION_PLAY_LAUGH
                component = ComponentName(context, SitcomWidgetProvider::class.java)
                addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            }
            return PendingIntent.getBroadcast(
                context,
                101,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}
