package com.mighty.sitcomwidget

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.annotation.RawRes

object SoundPlayer {
    private var activePlayer: MediaPlayer? = null

    fun play(context: Context, @RawRes soundResId: Int) {
        stop()
        val appContext = context.applicationContext
        val player = MediaPlayer.create(
            appContext,
            soundResId,
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build(),
            0
        ) ?: return

        activePlayer = player
        player.setOnCompletionListener { mp ->
            mp.release()
            if (activePlayer === mp) {
                activePlayer = null
            }
        }
        player.start()
    }

    fun stop() {
        try {
            activePlayer?.let { player ->
                if (player.isPlaying) {
                    player.stop()
                }
                player.release()
            }
        } catch (_: Exception) {
            // Ignore state exceptions on rapid taps
        } finally {
            activePlayer = null
        }
    }
}
