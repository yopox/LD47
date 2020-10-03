package com.yopox.ld47.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.Vector2
import com.yopox.ld47.LD47
import com.yopox.ld47.graphics.Fonts.fontTitle
import com.yopox.ld47.graphics.Fonts.drawCentered
import ktx.graphics.use

class Title(game: LD47): Screen(game) {

    override fun render(delta: Float) {
        super.render(delta)
        Gdx.gl.glClearColor(0.25f, 0.2f, 0.3f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.use {
            fontTitle.drawCentered(it, "STUCK IN A\nLOOP /////", Vector2.Zero, Vector2(WIDTH, HEIGHT))
        }
    }

    override fun reset() {}

    override fun inputDown(x: Float, y: Float) {}

    override fun inputUp(x: Float, y: Float) {}
}