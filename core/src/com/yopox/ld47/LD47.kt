package com.yopox.ld47

import com.badlogic.gdx.Screen
import com.yopox.ld47.graphics.Fonts
import com.yopox.ld47.screens.GameOver
import com.yopox.ld47.screens.InfiniteRace
import com.yopox.ld47.screens.LevelSelection
import com.yopox.ld47.screens.Title
import ktx.app.KtxGame
import kotlin.random.Random

class LD47 : KtxGame<Screen>() {

    companion object {
        val random = Random(System.currentTimeMillis())
    }

    override fun create() {
        super.create()

        Fonts.genFonts()

        addScreen(Title(this))
        addScreen(LevelSelection(this))
        addScreen(InfiniteRace(this))
        addScreen(GameOver(this))
        setScreen<Title>()
    }

    override fun render() {
        super.render()
        SoundManager.update()
    }

    override fun <Type : Screen> setScreen(type: Class<Type>) {
        if (currentScreen is com.yopox.ld47.screens.Screen)
                (currentScreen as com.yopox.ld47.screens.Screen).assetManager.clear()
        super.setScreen(type)
    }

}