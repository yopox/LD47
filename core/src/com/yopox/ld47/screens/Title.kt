package com.yopox.ld47.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.yopox.ld47.LD47

class Title(game: LD47): Screen(game) {

    override fun render(delta: Float) {
        super.render(delta)
        Gdx.gl.glClearColor(1f, 0.7f, 0.8f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    override fun reset() {}

    override fun inputDown(x: Float, y: Float) {}

    override fun inputUp(x: Float, y: Float) {}
}