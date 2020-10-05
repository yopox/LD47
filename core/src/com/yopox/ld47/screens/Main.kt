package com.yopox.ld47.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.yopox.ld47.Assets
import com.yopox.ld47.LD47
import com.yopox.ld47.Resources
import com.yopox.ld47.SoundManager
import com.yopox.ld47.entities.Boss
import com.yopox.ld47.entities.Orbital
import com.yopox.ld47.entities.Orbital.Companion.Facing.*
import com.yopox.ld47.entities.Player
import ktx.graphics.use

class Main(game: LD47) : Screen(game) {

    private val player = Player()
    private val boss = Boss()
    private val background = LD47.assetManager.get(Assets.sprites[Resources.BACKGROUND], Texture::class.java)

    override fun reset() {}

    override fun show() {
        super.show()
        SoundManager.play(Resources.OST_VROUM)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        player.update()
        boss.update()

        val collision = player.collidesWith(boss)
        if (collision != Orbital.Companion.Collision.NONE) player.hit(collision, boss)

        batch.use { batch ->
            // Background
            batch.draw(background, 0f, 0f)

            // Sprites
            player.draw(batch)
            boss.draw(batch)

            // Buttons
            buttons.forEach { button -> button.draw(batch) }
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) { renderer ->
            buttons.forEach { button -> button.drawBorder(renderer) }
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Line) { renderer ->
            renderer.color = Color.CYAN
            renderer.polygon(player.getCoordinates().first.toArray())
            renderer.polygon(player.getCoordinates().second.toArray())
            renderer.polygon(boss.getCoordinates().first.toArray())
            renderer.polygon(boss.getCoordinates().second.toArray())
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

    override fun keyTyped(character: Char): Boolean {
        when (character) {
            'm', 'M' -> SoundManager.mute()
        }
        return true
    }

}