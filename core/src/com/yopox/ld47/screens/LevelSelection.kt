package com.yopox.ld47.screens

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.yopox.ld47.*
import com.yopox.ld47.graphics.Button
import com.yopox.ld47.graphics.Fonts
import ktx.graphics.use

class LevelSelection(game: LD47) : Screen(game) {

    companion object {
        var selected = 0
            get() = field
    }

    private var background = Assets.getTexture(Levels.selected.background)
    private var car = Assets.getTexture(Levels.selected.car)

    init {
        buttons.add(Button("NEXT", Vector2(WIDTH - 256f, HEIGHT - 64f * 6 + 20f)) { next() })
        buttons.add(Button("PREVIOUS", Vector2(WIDTH - 256f, HEIGHT - 64f * 7)) { previous() })
        buttons.add(Button("SELECT", Vector2(WIDTH - 256f, HEIGHT - 64f * 8 - 20f)) { game.setScreen<InfiniteRace>() })
    }

    override fun reset() {}

    override fun show() {
        super.show()
        SoundManager.play(Resources.OST_GAME_OVER)
    }

    fun update() {
        background = Assets.getTexture(Levels.selected.background)
        car = Assets.getTexture(Levels.selected.car)
    }

    override fun render(delta: Float) {
        super.render(delta)

        batch.use { batch ->
            batch.draw(background, 0f, 0f)
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) { renderer ->
            renderer.color = Color.BLACK
            renderer.rect(128f, HEIGHT - 64f, 64f * 14, -64f * 2)
            renderer.rect(128f, HEIGHT - 64f * 4, 64f * 11, -64f * 6)
            renderer.rect(WIDTH - 128f, HEIGHT - 64f * 4.5f, -64f * 4, -64f * 5)

            buttons.forEach { it.drawBorder(renderer) }
        }

        batch.use { batch ->
            Fonts.fontTitle.draw(batch, Levels.selected.name, 64f * 3, HEIGHT - 64f - 36f)
            batch.draw(car, 64f * 3, HEIGHT - 64f * 6 + 32f)
            Fonts.fontItalic.draw(batch, Levels.selected.carName, 64f * 5.2f, HEIGHT - 64f * 5 - 24f + 32f)
            Fonts.font.draw(batch, Levels.selected.description, 64f * 3, HEIGHT - 64f * 7f + 48f)
            Fonts.fontItalic.draw(batch, "HIGH SCORE: ${Levels.selected.high}", 64f * 3, HEIGHT - 64f * 7f - 108f)

            buttons.forEach { it.draw(batch) }
        }

    }

    override fun keyTyped(character: Char): Boolean {
        when (character) {
            '\uF702' -> {
                SoundManager.sfx(Resources.SFX_BUTTON)
                previous()
            }
            '\uF703' -> {
                SoundManager.sfx(Resources.SFX_BUTTON)
                next()
            }
            ' ' -> game.setScreen<InfiniteRace>()
        }
        return true
    }

    fun next() {
        selected += 1
        if (selected > Levels.levels.lastIndex) selected = 0
        update()
    }

    fun previous() {
        selected -= 1
        if (selected < 0) selected = Levels.levels.lastIndex
        update()
    }

}