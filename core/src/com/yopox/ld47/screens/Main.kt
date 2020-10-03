package com.yopox.ld47.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.yopox.ld47.LD47
import com.yopox.ld47.entities.Orbital.Companion.Facing.*
import com.yopox.ld47.entities.Player
import ktx.graphics.use

class Main(game: LD47) : Screen(game) {

    private val colorLight = Color(0.55f, 0.5f, 0.6f, 1f)
    private val colorDark = Color(0.25f, 0.2f, 0.3f, 1f)

    companion object {
        const val RADIUS = 100f
        const val PLAYABLE_HEIGHT = 200f
    }

    private val player = Player()

    override fun reset() {}

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(colorDark.r, colorDark.g, colorDark.b, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        player.update()

        batch.use { batch ->
            // Background
            batch.draw(LD47.assetManager.get("circuit/global.png", Texture::class.java), 0f, 0f)

            // Sprites
            player.draw(batch)

            // Buttons
            buttons.forEach { button -> button.draw(batch) }
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) { renderer ->
            buttons.forEach { button -> button.drawBorder(renderer) }
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.LEFT -> player.facing = LEFT
            Input.Keys.RIGHT -> player.facing = RIGHT
        }
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.LEFT -> if (player.facing == LEFT) player.facing = FRONT
            Input.Keys.RIGHT -> if (player.facing == RIGHT) player.facing = FRONT
        }
        return true
    }

}