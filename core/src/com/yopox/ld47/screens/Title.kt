package com.yopox.ld47.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.yopox.ld47.Assets
import com.yopox.ld47.LD47
import com.yopox.ld47.Resources
import com.yopox.ld47.SoundManager
import com.yopox.ld47.graphics.Button
import ktx.graphics.use
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class Title(game: LD47) : Screen(game) {

    val background = Assets.getTexture(Resources.TITLE_BG)
    val title = Assets.getTexture(Resources.TITLE_TITLE)
    val sub = Assets.getTexture(Resources.TITLE_SUB)
    val cars = arrayOf(Assets.getTexture(Resources.TITLE_CAR1), Assets.getTexture(Resources.TITLE_CAR2), Assets.getTexture(Resources.TITLE_CAR3))

    var auto = cars.random()
    var autoPos = -WIDTH / 2

    var tick = 0

    init {
        buttons.add(
                Button("START", Vector2(WIDTH / 2, 100f)) {
                    game.setScreen<LevelSelection>()
                }
        )
    }

    override fun show() {
        super.show()
        tick = 0
        SoundManager.play(Resources.OST_TITLE)
    }

    override fun render(delta: Float) {
        tick += 1
        autoPos += 6f + sin((autoPos + WIDTH / 2) / WIDTH * 2 * PI).toFloat() * 3f

        Gdx.gl.glClearColor(0.25f, 0.2f, 0.3f, 1f)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)

        batch.use { batch ->
            batch.draw(background, 0f, 0f)
            batch.draw(title, WIDTH / 2 - title.width / 2, HEIGHT - 210f + sin(tick / 60f) * 15f)
            batch.draw(sub, WIDTH / 2 - sub.width / 2, HEIGHT - 475f + sin(tick / 60f) * 15f)
            batch.draw(auto, autoPos, 100f - auto.height / 2)

            buttons.forEach { button -> button.draw(batch) }
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) { renderer ->
            buttons.forEach { button -> button.drawBorder(renderer) }
        }

        if (autoPos >= 3 * WIDTH / 2) {
            auto = cars.random()
            autoPos = -WIDTH
        }
    }

    override fun reset() {}

    override fun keyTyped(character: Char): Boolean {
        when (character) {
            'm', 'M' -> SoundManager.mute()
            ' ', 'l' -> game.setScreen<LevelSelection>()
            //'o' -> game.setScreen<Ost>()
        }
        return true
    }

}