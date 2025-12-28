package com.flightgame.game

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.flightgame.R

class SoundManager(context: Context) {

    private val soundPool: SoundPool
    private var shootSoundId: Int = 0
    private var explosionSoundId: Int = 0
    private var powerUpSoundId: Int = 0

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        loadSounds(context)
    }

    private fun loadSounds(context: Context) {
        shootSoundId = soundPool.load(context, R.raw.laser1, 1)
        explosionSoundId = soundPool.load(context, R.raw.phaserdown1, 1)
        powerUpSoundId = soundPool.load(context, R.raw.powerup1, 1)
    }

    fun playShootSound() {
        soundPool.play(shootSoundId, 1f, 1f, 0, 0, 1f)
    }

    fun playExplosionSound() {
        soundPool.play(explosionSoundId, 1f, 1f, 0, 0, 1f)
    }

    fun playPowerUpSound() {
        soundPool.play(powerUpSoundId, 1f, 1f, 0, 0, 1f)
    }
}
