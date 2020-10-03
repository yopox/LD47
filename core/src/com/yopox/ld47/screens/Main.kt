package com.yopox.ld47.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.yopox.ld47.LD47
import com.yopox.ld47.graphics.Fonts
import com.yopox.ld47.graphics.Fonts.drawCentered
import ktx.graphics.use

class Main(game: LD47): Screen(game) {
    override fun reset() {}

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.25f, 0.2f, 0.3f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.use { batch ->
            buttons.forEach { button -> button.draw(batch) }
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) { renderer ->
            buttons.forEach { button -> button.drawBorder(renderer) }
        }
    }
}