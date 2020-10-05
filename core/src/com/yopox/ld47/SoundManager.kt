package com.yopox.ld47

import com.badlogic.gdx.audio.Music

object SoundManager {

    private var bgm: Music? = null
    private var sfx: Music? = null

    private val BGM_LEVEL = 0.5f
    private val SFX_LEVEL = 0.5f
    private var mute = false

    fun play(key: Resources) {
        val path = Assets.bgms[key] ?: return
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

    fun sfx(key: Resources) {
        val path = Assets.sfxs[key] ?: return
        sfx = LD47.assetManager.get(path, Music::class.java).also {
            it.play()
            it.volume = if (mute) 0f else SFX_LEVEL
            it.isLooping = false
        }
    }

    fun mute() {
        mute = !mute
        if (mute) {
            bgm?.volume = 0f
            sfx?.volume = 0f
        } else {
            bgm?.volume = BGM_LEVEL
            sfx?.volume = SFX_LEVEL
        }
    }

}