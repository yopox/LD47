package com.yopox.ld47.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.Vector2
import com.yopox.ld47.Fonts
import com.yopox.ld47.Fonts.drawCentered
import com.yopox.ld47.LD47
import ktx.graphics.use

class Title(game: LD47): Screen(game) {

    override fun render(delta: Float) {
        super.render(delta)
        Gdx.gl.glClearColor(1f, 0.7f, 0.8f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.use {
            Fonts.fontRoundAbout.drawCentered(it, "Stuck in a loop", Vector2.Zero, Vector2(WIDTH, HEIGHT))
        }
    }

    override fun reset() {}

    override fun inputDown(x: Float, y: Float) {}

    override fun inputUp(x: Float, y: Float) {}
}