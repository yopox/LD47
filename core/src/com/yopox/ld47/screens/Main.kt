package com.yopox.ld47.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.yopox.ld47.LD47
import com.yopox.ld47.entities.Player
import ktx.graphics.use

class Main(game: LD47) : Screen(game) {

    private val colorLight = Color(0.55f, 0.5f, 0.6f, 1f)
    private val colorDark = Color(0.25f, 0.2f, 0.3f, 1f)

    companion object {
        const val RADIUS = 100f
        const val PLAYABLE_HEIGHT = 200f

        val LEFT_PLANET = Vector2(WIDTH / 3, HEIGHT / 2)
        val RIGHT_PLANET = Vector2(WIDTH * 2 / 3, HEIGHT / 2)
    }

    private val player = Player()

    override fun reset() {}

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(colorDark.r, colorDark.g, colorDark.b, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        player.update()

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) { renderer ->
            renderer.color = colorLight
            renderer.circle(LEFT_PLANET.x, LEFT_PLANET.y, RADIUS + PLAYABLE_HEIGHT)
            renderer.circle(RIGHT_PLANET.x, RIGHT_PLANET.y, RADIUS + PLAYABLE_HEIGHT)

            renderer.color = colorDark
            renderer.circle(LEFT_PLANET.x, LEFT_PLANET.y, RADIUS)
            renderer.circle(RIGHT_PLANET.x, RIGHT_PLANET.y, RADIUS)
        }

        batch.use { batch ->
            buttons.forEach { button -> button.draw(batch) }
            player.draw(batch)
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) { renderer ->
            buttons.forEach { button -> button.drawBorder(renderer) }
        }
    }
}