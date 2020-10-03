package com.yopox.ld47

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Screen
import com.yopox.ld47.screens.Title
import ktx.app.KtxGame

class LD47 : KtxGame<Screen>() {

    override fun create() {
        super.create()
        addScreen(Title(this))
        setScreen<Title>()
    }

}