package com.yopox.ld47.screens

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.yopox.ld47.*
import com.yopox.ld47.entities.*
import com.yopox.ld47.entities.Orbital.Companion.Facing.*
import com.yopox.ld47.graphics.Fonts
import com.yopox.ld47.graphics.PadButton
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

    override val screenAssets
        get() = arrayOf(
                Levels.selected.background,
                Levels.selected.car,
                Levels.selected.enemy,
                Resources.CAR_BOSS,

                Resources.GUI_BG,
                Resources.GUI_BG2,
        ).plus(Resources.bonuses)

    private lateinit var player: Player
    private var enemies = arrayListOf<Orbital>()
    private var bonuses = arrayListOf<Bonus>()
    private var state = State.COUNT
    private var internalFrame = 0
    private var sequencerState = SequencerState.NORMAL
    private var initButtons = false

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
        player = Player(getTexture(Levels.selected.car))
        enemies.clear()
        bonuses.clear()
        internalFrame = 0
        blockInput = false
        SoundManager.stop()
    }

    override fun show() {
        super.show()
        reset()
        if (!initButtons) {
            if (Gdx.app.type == Application.ApplicationType.Android) {
                //val dx = viewport.leftGutterWidth
                val dx = 0
                buttons.add(PadButton("<-", Vector2(PadButton.SIZE - dx, PadButton.SIZE), { player.facing = LEFT }, { player.facing = FRONT }))
                buttons.add(PadButton("->", Vector2(WIDTH - PadButton.SIZE + dx, PadButton.SIZE), { player.facing = RIGHT }, { player.facing = FRONT }))
                buttons.add(PadButton("NITRO", Vector2(PadButton.SIZE - dx, PadButton.SIZE * 2.5f), {}, { player.nitro() }))
                buttons.add(PadButton("BRAKE", Vector2(WIDTH - PadButton.SIZE + dx, PadButton.SIZE * 2.5f), {}, { player.brake() }))
            }
            initButtons = true
        }
    }

    override fun render(delta: Float) {
        super.render(delta)

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
                    16 -> SoundManager.sfx(SFX.START)
                    170 -> {
                        state = State.INFINITE
                        SoundManager.play(BGM.LEVEL)
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
            SoundManager.sfx(SFX.GAME_OVER)
        }
    }

    private fun updateSequencer() {
        internalFrame += 1

        if (internalFrame % Levels.selected.tick == 0) {
            // Spawn a bonus
            if (LD47.random.nextFloat() <= BONUS_PROBABILITY && bonuses.size < 2) {
                val bonus = Resources.bonuses.random()
                bonuses.add(Bonus(getTexture(bonus), bonus))
            }

            // Spawn a crosser
            if (LD47.random.nextFloat() <= CROSSER_PROBABILITY) enemies.add(Crosser(getTexture(Levels.selected.enemy)))
        }

        if (internalFrame >= 1800 && !sequencerState.isBoss) {
            if (LD47.random.nextFloat() <= ALT_PROBABILITY) {
                sequencerState = SequencerState.BOSS_ALT
            } else {
                sequencerState = SequencerState.BOSS
            }
            internalFrame = 0
            enemies.add(Boss(getTexture(Resources.CAR_BOSS), if (sequencerState == SequencerState.BOSS_ALT) 5 else 3))
            SoundManager.stop()
            SoundManager.sfx(SFX.BOSS)
        }

        if (internalFrame > 100 && sequencerState.isBoss && enemies.none { it is Boss }) {
            if (LD47.random.nextFloat() <= ALT_PROBABILITY) {
                sequencerState = SequencerState.NORMAL_ALT
                SoundManager.play(BGM.LEVEL_ALT)
            } else {
                sequencerState = SequencerState.NORMAL
                SoundManager.play(BGM.LEVEL)
            }
            internalFrame = 0
        }

        if (sequencerState.isBoss && internalFrame == 100) {
            SoundManager.play(if (sequencerState == SequencerState.BOSS) BGM.BOSS else BGM.BOSS_ALT)
        }

    }

    private fun drawGame() {
        batch.use { batch ->
            // Background
            batch.draw(getTexture(Levels.levels[LevelSelection.selected].background), 0f, 0f)

            // Sprites
            bonuses.forEach { it.draw(batch) }
            player.draw(batch)
            enemies.forEach { it.draw(batch) }

        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) { renderer ->
            buttons.forEach { button -> button.drawBackground(renderer) }
        }

        val gui_bg = getTexture(Resources.GUI_BG)
        val gui_bg2 = getTexture(Resources.GUI_BG2)

        batch.use { batch ->

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
            renderer.rect(0f, HEIGHT - gui_bg.height - 3f, player.nitroCounter * 4, 6f)
        }
    }

    private fun updateEntities() {
        player.update()
        myScore = myScore.add(BigDecimal.valueOf(player.speed.pow(2).toDouble()))

        enemies.forEach {
            it.update()
            val collision = player.collidesWith(it)
            if (collision != Orbital.Companion.Collision.NONE) {
                player.hit(collision, it)
                SoundManager.sfx(SFX.HIT)
            }

            if (!it.hit) {
                for (enemy in enemies) {
                    if (enemy != it) {
                        val collision = enemy.collidesWith(it)
                        if (collision != Orbital.Companion.Collision.NONE) {
                            it.hit(collision)
                            enemy.hit(collision)
                            SoundManager.sfx(SFX.HIT)
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
            it.update()
            val collision = player.collidesWith(it)
            if (collision != Orbital.Companion.Collision.NONE) {
                SoundManager.sfx(SFX.SELECT)
                player.collect(it)
            }
        }
        bonuses.filter { it.toDestroy }.forEach { bonuses.remove(it) }
    }

    private fun bossDefeated() {
        SoundManager.play(BGM.LEVEL_ALT)
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
                        SoundManager.sfx(SFX.PAUSE)
                    }
                    State.PAUSE -> {
                        state = State.INFINITE
                        SoundManager.sfx(SFX.UNPAUSE)
                    }
                    else -> Unit
                }
            }
            ' ', '\uF700' -> player.nitro()
            'x', '\uF701' -> player.brake()
        }
        return true
    }

}