package com.yopox.ld47

import com.badlogic.gdx.audio.Music

object SoundManager {

    enum class State {
        OFF, BGM, BGM_OUT, BGM_IN, NEXT_BGM
    }

    private var bgm: Music? = null
    private var sfx: Music? = null

    private var state = State.OFF
    private var next_bgm: Resources? = null

    private val BGM_LEVEL = 0.5f
    private var currentBgmLevel = BGM_LEVEL
    private val SFX_LEVEL = 0.5f
    private var mute = false

    fun update() {
        when (state) {
            State.BGM_OUT -> {
                bgm?.let {
                    currentBgmLevel -= 0.025f
                    it.volume = if (mute) 0f else currentBgmLevel
                    if (currentBgmLevel < 0.025f) {
                        state = if (next_bgm != null) State.NEXT_BGM else State.OFF
                    }
                }
                if (state == State.OFF) {
                    bgm?.dispose()
                }
            }
            State.BGM_IN -> {
                bgm?.let {
                    currentBgmLevel += 0.05f
                    it.volume = if (mute) 0f else currentBgmLevel
                    if (currentBgmLevel > BGM_LEVEL) {
                        state = State.BGM
                    }
                }
            }
            State.NEXT_BGM -> {
                bgm?.let {
                    it.stop()
                    it.dispose()
                }
                bgm = LD47.assetManager.get(Assets.bgms[next_bgm], Music::class.java).also {
                    it.play()
                    it.volume = 0f
                    it.isLooping = true
                }
                state = State.BGM_IN
            }
        }
    }

    fun play(key: Resources) {
        val path = Assets.bgms[key] ?: return
        when (state) {
            State.OFF -> {
                bgm = LD47.assetManager.get(path, Music::class.java).also {
                    it.play()
                    it.volume = if (mute) 0f else BGM_LEVEL
                    it.isLooping = true
                }
                currentBgmLevel = BGM_LEVEL
                state = State.BGM
            }
            else -> {
                next_bgm = key
                state = State.BGM_OUT
            }

        }
    }

    fun sfx(key: Resources) {
        val path = Assets.sfxs[key] ?: return
        sfx?.let {
            it.stop()
            it.dispose()
        }
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

    fun stop() {
        state = State.BGM_OUT
        next_bgm = null
    }

}