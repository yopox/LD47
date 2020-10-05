package com.yopox.ld47.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.yopox.ld47.LD47
import com.yopox.ld47.Levels
import com.yopox.ld47.Resources
import com.yopox.ld47.SoundManager
import com.yopox.ld47.graphics.Button
import com.yopox.ld47.graphics.Fonts
import com.yopox.ld47.graphics.Fonts.drawCentered
import ktx.graphics.use

class GameOver(game: LD47) : Screen(game) {

    enum class State {
        FADE_IN, WAIT
    }

    private var gameOverFrame = 0
    private var state = State.FADE_IN
    private var newHigh = false
    private var score = ""

    init {
        buttons.add(Button("TITLE", Vector2(WIDTH / 2, HEIGHT / 5)) { game.setScreen<Title>() })
    }

    override fun show() {
        super.show()
        SoundManager.play(Resources.OST_GAME_OVER)
        reset()
        score = InfiniteRace.scoreFormatter.format(InfiniteRace.score)
        val high = Levels.selected.high
        if (score > high) {
            newHigh = true
            Levels.selected.high = score
        }
    }

    override fun reset() {
        gameOverFrame = 0
        state = State.FADE_IN
        blockInput = true
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)

        batch.use { batch ->
            Fonts.fontTitle.drawCentered(batch, "GAME OVER", Vector2(0f, HEIGHT / 2), Vector2(WIDTH,  HEIGHT / 2))
            Fonts.fontItalic.drawCentered(batch, Levels.selected.name, Vector2(0f, HEIGHT / 4 + 24f), Vector2(WIDTH,  HEIGHT / 2))
            Fonts.fontItalic.drawCentered(batch, "SCORE: $score" + if (newHigh) "      " else "", Vector2(0f, HEIGHT / 4 - 24f), Vector2(WIDTH,  HEIGHT / 2))
            Fonts.fontItalic.drawCentered(batch, "HIGH:  " + Levels.selected.high + if (newHigh) " [NEW!]" else "", Vector2(0f, HEIGHT / 4 - 64f), Vector2(WIDTH,  HEIGHT / 2))
            buttons.forEach { it.draw(batch) }
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) { renderer ->
            buttons.forEach { it.drawBorder(renderer) }
        }

        when (state) {
            State.FADE_IN -> {
                Gdx.gl.glEnable(GL30.GL_BLEND)
                Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA)
                shapeRenderer.use(ShapeRenderer.ShapeType.Filled) { renderer ->
                    renderer.color = Color(0f, 0f, 0f, 1 - 0.02f * gameOverFrame)
                    renderer.rect(0f, 0f, WIDTH, HEIGHT)
                }
                Gdx.gl.glDisable(GL30.GL_BLEND)

                gameOverFrame += 1

                if (0.02f * gameOverFrame > 0.98) {
                    state = State.WAIT
                    blockInput = false
                }
            }
        }

    }

}