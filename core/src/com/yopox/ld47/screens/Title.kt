package com.yopox.ld47.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.yopox.ld47.Assets
import com.yopox.ld47.LD47
import com.yopox.ld47.Resources
import com.yopox.ld47.SoundManager
import com.yopox.ld47.graphics.Button
import com.yopox.ld47.graphics.Fonts.drawCentered
import com.yopox.ld47.graphics.Fonts.fontTitle
import ktx.graphics.use

class Title(game: LD47) : Screen(game) {

    init {
        buttons.add(
                Button("START", Vector2(WIDTH / 2, HEIGHT / 4)) {
                    game.setScreen<Main>()
                }
        )
    }

    override fun show() {
        super.show()
        SoundManager.play(Resources.OST_TITLE)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.25f, 0.2f, 0.3f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.use { batch ->
            fontTitle.drawCentered(batch, "STUCK IN A\nLOOP /////",
                    Vector2(0f, HEIGHT / 3), Vector2(WIDTH, HEIGHT * 2 / 3))
            buttons.forEach { button -> button.draw(batch) }
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) { renderer ->
            buttons.forEach { button -> button.drawBorder(renderer) }
        }
    }

    override fun reset() {}

    override fun keyTyped(character: Char): Boolean {
        when (character) {
            'm', 'M' -> SoundManager.mute()
        }
        return true
    }

}