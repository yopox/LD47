package com.yopox.ld47

import com.badlogic.gdx.audio.Music

object SoundManager {

    private var bgm: Music? = null
    private var sfx: Music? = null

    private val BGM_LEVEL = 0.5f
    private val SFX_LEVEL = 0.5f
    private var mute = false

    fun play(key: Resources) {
        val path = Assets.sounds[key] ?: return
        bgm?.let {
            it.stop()
            it.dispose()
        }
        bgm = LD47.assetManager.get(path, Music::class.java).also {
            it.play()
            it.volume = if (mute) 0f else BGM_LEVEL
            it.isLooping = true
        }
    }

    fun mute() { mute = !mute }

}