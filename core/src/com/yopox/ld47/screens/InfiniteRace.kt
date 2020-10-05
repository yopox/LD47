package com.yopox.ld47.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.yopox.ld47.*
import com.yopox.ld47.entities.*
import com.yopox.ld47.entities.Orbital.Companion.Facing.*
import com.yopox.ld47.graphics.Fonts
import ktx.graphics.use
import java.math.BigDecimal
import java.text.DecimalFormat
import kotlin.math.pow

class InfiniteRace(game: LD47) : Screen(game) {

    enum class State {
        COUNT, INFINITE, PAUSE, GAME_OVER
    }

    enum class SequencerState {
        NORMAL, BOSS, NORMAL_ALT, BOSS_ALT
    }

    val SequencerState.isBoss: Boolean
    get() = when (this) {
        SequencerState.NORMAL, SequencerState.NORMAL_ALT -> false
        else -> true
    }

    private var player = Player()
    private var enemies = arrayListOf<Orbital>()
    private var bonuses = arrayListOf<Bonus>()
    private var background = Assets.getTexture(Levels.levels[LevelSelection.selected].background)
    private val gui_bg = Assets.getTexture(Resources.GUI_BG)
    private val gui_bg2 = Assets.getTexture(Resources.GUI_BG2)
    private var state = State.COUNT
    private var internalFrame = 0
    private var sequencerState = SequencerState.NORMAL

    companion object {
        val scoreFormatter = DecimalFormat("000000000")
        val speedFormatter = DecimalFormat("000")
        private var myScore: BigDecimal = BigDecimal.ZERO

        private val BONUS_PROBABILITY = 0.25f
        private val CROSSER_PROBABILITY = 0.35f
        private val ALT_PROBABILITY = 0.25f

        val score: BigDecimal get() = myScore
    }

    override fun reset() {
        state = State.COUNT
        myScore = BigDecimal.ZERO
        player = Player()
        enemies.clear()
        bonuses.clear()
        background = Assets.getTexture(Levels.selected.background)
        internalFrame = 0
        blockInput = false
        SoundManager.stop()
    }

    override fun show() {
        super.show()
        reset()
    }

    override fun render(delta: Float) {

        when (state) {
            State.INFINITE -> {
                updateSequencer()
                updateEntities()
                drawGame()
            }
            State.COUNT -> {
                drawGame()
                internalFrame += 1
                when (internalFrame) {
                    16 ->  SoundManager.sfx(Resources.SFX_321)
                    170 -> {
                        state = State.INFINITE
                        SoundManager.play(Resources.OST_LEVEL)
                        internalFrame = 0
                    }
                }
            }
            State.PAUSE -> {
                drawGame()
            }
            State.GAME_OVER -> {
                drawGame()
                internalFrame += 1
                Gdx.gl.glEnable(GL30.GL_BLEND)
                Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA)
                shapeRenderer.use(ShapeRenderer.ShapeType.Filled) { renderer ->
                    renderer.color = Color(0f, 0f, 0f, 0.006f * internalFrame)
                    renderer.rect(0f, 0f, WIDTH, HEIGHT)
                }
                Gdx.gl.glDisable(GL30.GL_BLEND)

                if (0.004f * internalFrame > 0.98) {
                    game.setScreen<GameOver>()
                }
            }
        }

        // Game Over
        if (player.nitroCounter < 0 && state == State.INFINITE) {
            state = State.GAME_OVER
            internalFrame = 0
            blockInput = true
            SoundManager.stop()
            SoundManager.sfx(Resources.SFX_GAME_OVER)
        }
    }

    private fun updateSequencer() {
        internalFrame += 1

        if (internalFrame % 120 == 0) {
            // Spawn a bonus
            if (LD47.random.nextFloat() <= BONUS_PROBABILITY && bonuses.size < 2) bonuses.add(Bonus())

            // Spawn a crosser
            if (LD47.random.nextFloat() <= CROSSER_PROBABILITY) enemies.add(Crosser())

        }

        if (internalFrame >= 1800 && !sequencerState.isBoss) {
            if (LD47.random.nextFloat() <= ALT_PROBABILITY) {
                sequencerState = SequencerState.BOSS_ALT
            } else {
                sequencerState = SequencerState.BOSS
            }
            internalFrame = 0
            enemies.add(Boss())
            SoundManager.stop()
            SoundManager.sfx(Resources.SFX_BOSS)
        }

        if (internalFrame > 100 && sequencerState.isBoss && enemies.none { it is Boss }) {
            if (LD47.random.nextFloat() <= ALT_PROBABILITY) {
                sequencerState = SequencerState.NORMAL_ALT
                SoundManager.play(Resources.OST_LEVEL_ALT)
            } else {
                sequencerState = SequencerState.NORMAL
                SoundManager.play(Resources.OST_LEVEL)
            }
            internalFrame = 0
        }

        if (sequencerState.isBoss && internalFrame == 100) {
            SoundManager.play(if (sequencerState == SequencerState.BOSS) Resources.OST_BOSS else Resources.OST_BOSS_ALT)
        }

    }

    private fun drawGame() {
        batch.use { batch ->
            // Background
            batch.draw(background, 0f, 0f)

            // Sprites
            bonuses.forEach { it.draw(batch) }
            player.draw(batch)
            enemies.forEach { it.draw(batch) }

            // GUI
            batch.draw(gui_bg, 0f, HEIGHT - gui_bg.height)
            batch.draw(gui_bg2, WIDTH - gui_bg2.width, HEIGHT - gui_bg.height)
            buttons.forEach { button -> button.draw(batch) }
            Fonts.fontItalic.draw(batch, scoreFormatter.format(myScore), 32f, HEIGHT - 54f)
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
        }
    }

    private fun updateEntities() {
        player.update()
        myScore = myScore.add(BigDecimal.valueOf(player.speed.pow(2).toDouble()))

        enemies.forEach {
            it.update()
            val collision = player.collidesWith(it)
            if (collision != Orbital.Companion.Collision.NONE) player.hit(collision, it)

            if (!it.hit) {
                for (enemy in enemies) {
                    if (enemy != it) {
                        val collision = enemy.collidesWith(it)
                        if (collision != Orbital.Companion.Collision.NONE) {
                            it.hit(collision)
                            enemy.hit(collision)
                        }
                    }
                }
            }
        }
        enemies.filter { it.toDestroy }.forEach {
            if (it is Boss) bossDefeated()
            enemies.remove(it)
        }

        bonuses.forEach {
            val collision = player.collidesWith(it)
            if (collision != Orbital.Companion.Collision.NONE) player.collect(it)
        }
        bonuses.filter { it.toDestroy }.forEach { bonuses.remove(it) }
    }

    private fun bossDefeated() {
        SoundManager.play(Resources.OST_LEVEL_ALT)
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
                    State.INFINITE -> {
                        state = State.PAUSE
                        SoundManager.sfx(Resources.SFX_PAUSE)
                    }
                    State.PAUSE -> {
                        state = State.INFINITE
                        SoundManager.sfx(Resources.SFX_UNPAUSE)
                    }
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