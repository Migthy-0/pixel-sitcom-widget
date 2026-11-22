package com.mighty.sitcomwidget

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

object SoundPlayer {
    private var soundPool: SoundPool? = null
    private var laughSoundId: Int = 0
    private var activeStreamId: Int = 0
    @Volatile
    private var isLoaded: Boolean = false
    @Volatile
    private var playWhenLoaded: Boolean = false

    @Synchronized
    fun init(context: Context) {
        if (soundPool != null) return

        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val pool = SoundPool.Builder()
            .setMaxStreams(2)
            .setAudioAttributes(attributes)
            .build()

        pool.setOnLoadCompleteListener { sp, sampleId, status ->
            if (status == 0 && sampleId == laughSoundId) {
                isLoaded = true
                if (playWhenLoaded) {
                    playWhenLoaded = false
                    activeStreamId = sp.play(laughSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
                }
            }
        }

        laughSoundId = pool.load(context.applicationContext, R.raw.sitcom_laugh, 1)
        soundPool = pool
    }

    fun play(context: Context) {
        if (soundPool == null) {
            playWhenLoaded = true
            init(context)
            return
        }

        val pool = soundPool ?: return
        if (isLoaded) {
            if (activeStreamId != 0) {
                pool.stop(activeStreamId)
            }
            activeStreamId = pool.play(laughSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
        } else {
            playWhenLoaded = true
        }
    }
}
