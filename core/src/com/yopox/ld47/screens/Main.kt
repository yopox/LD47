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
import com.yopox.ld47.graphics.Fonts
import ktx.graphics.use
import java.math.BigDecimal
import java.text.DecimalFormat
import kotlin.math.pow

class Main(game: LD47) : Screen(game) {

    enum class State {
        INFINITE, PAUSE, GAME_OVER
    }

    private var player = Player()
    private var enemies = arrayListOf<Orbital>()
    private val background = LD47.assetManager.get(Assets.sprites[Resources.BACKGROUND], Texture::class.java)
    private val gui_bg = LD47.assetManager.get(Assets.sprites[Resources.GUI_BG], Texture::class.java)
    private val gui_bg2 = LD47.assetManager.get(Assets.sprites[Resources.GUI_BG2], Texture::class.java)
    private var score = BigDecimal.ZERO
    private val scoreFormatter = DecimalFormat("000000000")
    private val speedFormatter = DecimalFormat("000")
    private var state = State.INFINITE

    override fun reset() {
        state = State.INFINITE
        score = BigDecimal.ZERO
        player = Player()
        enemies.clear()
        enemies.add(Boss())
    }

    override fun show() {
        super.show()
        SoundManager.play(Resources.OST_VROUM)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        when (state) {
            State.INFINITE -> {
                updateEntities()
                drawGame()
            }
            State.PAUSE -> {
                drawGame()
            }
            State.GAME_OVER -> {
                drawGame()
            }
        }

        // Game Over
        if (player.nitroCounter < 0) state = State.GAME_OVER
    }

    private fun drawGame() {
        batch.use { batch ->
            // Background
            batch.draw(background, 0f, 0f)

            // Sprites
            player.draw(batch)
            enemies.forEach { it.draw(batch) }

            // GUI
            batch.draw(gui_bg, 0f, HEIGHT - gui_bg.height)
            batch.draw(gui_bg2, WIDTH - gui_bg2.width, HEIGHT - gui_bg.height)
            buttons.forEach { button -> button.draw(batch) }
            Fonts.fontItalic.draw(batch, scoreFormatter.format(score), 32f, HEIGHT - 54f)
            Fonts.fontSmall.draw(batch, "SCORE", 32f, HEIGHT - 20f)

            Fonts.fontItalic.draw(batch, speedFormatter.format(player.speed * 30) + " km/h", WIDTH - gui_bg2.width + 96f, HEIGHT - 54f)
            Fonts.fontSmall.draw(batch, "SPEED", WIDTH - gui_bg2.width + 188f, HEIGHT - 20f)
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) { renderer ->
            buttons.forEach { button -> button.drawBorder(renderer) }

            renderer.color = Color.CYAN
            renderer.rect(0f, HEIGHT - gui_bg.height - 4f, player.nitroCounter * 4, 4f)
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Line) { renderer ->
            renderer.color = Color.CYAN
/*          renderer.polygon(player.getCoordinates().first.toArray())
            renderer.polygon(player.getCoordinates().second.toArray())
            enemies.forEach {
                renderer.polygon(it.getCoordinates().first.toArray())
                renderer.polygon(it.getCoordinates().second.toArray())
            }*/
        }
    }

    private fun updateEntities() {
        player.update()
        score = score.add(BigDecimal.valueOf(player.speed.pow(2).toDouble()))

        enemies.forEach {
            it.update()
            val collision = player.collidesWith(it)
            if (collision != Orbital.Companion.Collision.NONE) player.hit(collision, it)
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
            'm' -> SoundManager.mute()
            '\u001B' -> {
                when (state) {
                    State.INFINITE -> state = State.PAUSE
                    State.PAUSE -> state = State.INFINITE
                    else -> Unit
                }
            }
            ' ' -> player.nitro()
            'x' -> player.brake()
            'r' -> reset()
        }
        return true
    }

}