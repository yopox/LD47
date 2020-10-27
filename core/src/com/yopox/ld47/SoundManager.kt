package com.yopox.ld47

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music

object SoundManager {

    enum class State {
        OFF, PLAYING, BGM_OUT, BGM_IN, NEXT_BGM
    }

    private var bgm: Music? = null
    private var sfx: Music? = null

    private var state = State.OFF
    private var next_bgm: BGM? = null

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
                        state = State.PLAYING
                    }
                }
            }
            State.NEXT_BGM -> {
                bgm?.let {
                    it.stop()
                    it.dispose()
                }
                bgm = Gdx.audio.newMusic(Gdx.files.internal(Assets.bgms[next_bgm])).also {
                    it.play()
                    it.volume = 0f
                    it.isLooping = true
                }
                state = State.BGM_IN
            }
        }
    }

    fun play(key: BGM) {
        val path = Assets.bgms[key] ?: return
        when (state) {
            State.OFF -> {
                bgm = Gdx.audio.newMusic(Gdx.files.internal(path)).also {
                    it.play()
                    it.volume = if (mute) 0f else BGM_LEVEL
                    it.isLooping = true
                }
                currentBgmLevel = BGM_LEVEL
                state = State.PLAYING
            }
            else -> {
                next_bgm = key
                state = State.BGM_OUT
            }

        }
    }

    fun sfx(key: SFX) {
        val path = Assets.sfxs[key] ?: return
        sfx?.let {
            it.stop()
            it.dispose()
        }
        sfx = Gdx.audio.newMusic(Gdx.files.internal(path)).also {
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