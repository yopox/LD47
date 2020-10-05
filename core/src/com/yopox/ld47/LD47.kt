package com.yopox.ld47

import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Texture
import com.yopox.ld47.graphics.Fonts
import com.yopox.ld47.screens.GameOver
import com.yopox.ld47.screens.LevelSelection
import com.yopox.ld47.screens.InfiniteRace
import com.yopox.ld47.screens.Title
import ktx.app.KtxGame
import kotlin.random.Random

class LD47 : KtxGame<Screen>() {

    companion object {
        val assetManager = AssetManager()
        val random = Random(System.currentTimeMillis())
    }

    override fun create() {
        super.create()

        Fonts.genFonts()
        with(assetManager) {
            for ((_, path) in Assets.sprites) { load(path, Texture::class.java) }
            for ((_, path) in Assets.bgms) { load(path, Music::class.java) }
            for ((_, path) in Assets.sfxs) { load(path, Music::class.java) }
            finishLoading()
        }

        addScreen(Title(this))
        addScreen(LevelSelection(this))
        addScreen(InfiniteRace(this))
        addScreen(GameOver(this))
        setScreen<InfiniteRace>()
    }

    override fun render() {
        super.render()
        SoundManager.update()
    }

}