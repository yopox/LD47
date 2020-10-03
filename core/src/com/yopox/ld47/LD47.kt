package com.yopox.ld47

import com.badlogic.gdx.Screen
import com.yopox.ld47.graphics.Fonts
import com.yopox.ld47.screens.Main
import com.yopox.ld47.screens.Title
import ktx.app.KtxGame

class LD47 : KtxGame<Screen>() {

    override fun create() {
        super.create()

        Fonts.genFonts()

        addScreen(Title(this))
        addScreen(Main(this))
        setScreen<Title>()
    }

}