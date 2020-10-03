package com.yopox.ld47

import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.yopox.ld47.graphics.Fonts
import com.yopox.ld47.screens.Main
import com.yopox.ld47.screens.Title
import ktx.app.KtxGame

class LD47 : KtxGame<Screen>() {

    companion object {
        val assetManager = AssetManager()
    }

    override fun create() {
        super.create()

        Fonts.genFonts()
        with(assetManager) {
            load("sprites/ship.png", Texture::class.java)
            finishLoading()
        }

        addScreen(Title(this))
        addScreen(Main(this))
        setScreen<Title>()
    }

}