package com.mighty.sitcomwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class SitcomWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_PLAY_LAUGH) {
            SoundPlayer.play(context, R.raw.sitcom_laugh)
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
