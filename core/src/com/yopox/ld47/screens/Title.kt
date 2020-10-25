package com.yopox.ld47.screens

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.yopox.ld47.LD47
import com.yopox.ld47.Resources
import com.yopox.ld47.SoundManager
import com.yopox.ld47.graphics.MenuButton
import ktx.graphics.use
import kotlin.math.PI
import kotlin.math.sin

class Title(game: LD47) : Screen(game) {

    override val screenAssets
        get() = arrayOf(
                Resources.TITLE_BG,
                Resources.TITLE_TITLE,
                Resources.TITLE_SUB,
                Resources.OST_TITLE,
        ).plus(Resources.titleCars)

    var autoPos = -WIDTH / 2
    var auto = Resources.titleCars.random()
    var tick = 0

    init {
        buttons.add(
                MenuButton("START", Vector2(WIDTH / 2, 100f)) {
                    game.setScreen<LevelSelection>()
                }
        )
    }

    override fun reset() {
        autoPos = -WIDTH / 2
        tick = 0
    }

    override fun show() {
        super.show()
        tick = 0
        SoundManager.play(assetManager, Resources.OST_TITLE)
    }

    override fun render(delta: Float) {
        super.render(delta)

        tick += 1
        autoPos += 6f + sin((autoPos + WIDTH / 2) / WIDTH * 2 * PI).toFloat() * 3f
        val bg = getTexture(Resources.TITLE_BG)
        val title = getTexture(Resources.TITLE_TITLE)
        val sub = getTexture(Resources.TITLE_SUB)
        val autoTexture = getTexture(auto)

        batch.use { batch ->
            batch.draw(bg, 0f, 0f)
            batch.draw(title, WIDTH / 2 - title.width / 2, HEIGHT - 210f + sin(tick / 60f) * 15f)
            batch.draw(sub, WIDTH / 2 - sub.width / 2, HEIGHT - 475f + sin(tick / 60f) * 15f)
            batch.draw(autoTexture, autoPos, 100f - autoTexture.height / 2)

            buttons.forEach { button -> button.draw(batch) }
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) { renderer ->
            buttons.forEach { button -> button.drawBorder(renderer) }
        }

        if (autoPos >= 3 * WIDTH / 2) {
            auto = Resources.titleCars.random()
            autoPos = -WIDTH
        }
    }

    override fun keyTyped(character: Char): Boolean {
        when (character) {
            'm', 'M' -> SoundManager.mute()
            ' ', 'l' -> game.setScreen<LevelSelection>()
            //'o' -> game.setScreen<Ost>()
        }
        return true
    }

}