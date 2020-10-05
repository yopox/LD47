package com.yopox.ld47.graphics

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.yopox.ld47.Resources
import com.yopox.ld47.SoundManager
import ktx.math.div
import ktx.math.minus

/**
 * Represents a text button in the UI.
 * [pos] are the coordinates of the center of the button.
 */
class Button(val text: String, val pos: Vector2, val callback: () -> Unit) {

    companion object {
        private val SIZE = Vector2(256f, 96f)
        const val WIDTH = 2f
    }

    private var selected = false
    private val origin = pos - SIZE / 2
    private var clicked = false

    val glyphLayout = GlyphLayout().apply {
        setText(Fonts.font, text)
    }

    fun contains(x: Float, y: Float) = x > pos.x - SIZE.x / 2
            && x < pos.x + SIZE.x / 2
            && y > pos.y - SIZE.y / 2
            && y < pos.y + SIZE.y / 2

    fun updateSelected(mouseX: Float, mouseY: Float) {
        selected = contains(mouseX, mouseY)
    }

    fun click(mouseX: Float, mouseY: Float) {
        if (contains(mouseX, mouseY)) clicked = true
    }

    fun release(mouseX: Float, mouseY: Float) {
        if (clicked && contains(mouseX, mouseY)) {
            clicked = false
            callback()
            SoundManager.sfx(Resources.SFX_BUTTON)
        }
    }

    fun draw(batch: Batch) {
        val font = if (selected) Fonts.fontItalic else Fonts.font
        font.draw(batch, text, pos.x - glyphLayout.width / 2, pos.y + glyphLayout.height / 2)
    }

    fun drawBorder(shapeRenderer: ShapeRenderer) {
        if (selected) {
            if (clicked) shapeRenderer.color = Color.CORAL
            shapeRenderer.rect(origin.x, origin.y, SIZE.x, WIDTH)
            shapeRenderer.rect(origin.x, origin.y, WIDTH, SIZE.y)
            shapeRenderer.rect(origin.x + SIZE.x - WIDTH, origin.y, WIDTH, SIZE.y)
            shapeRenderer.rect(origin.x, origin.y + SIZE.y - WIDTH, SIZE.x, WIDTH)
        }
    }

}