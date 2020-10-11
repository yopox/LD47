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
 * [callback] called on button release.
 */
abstract class Button(val text: String, val pos: Vector2, val size: Vector2) {

    companion object {
        const val WIDTH = 2f
    }

    private var hovered = false
    private val origin = pos - size / 2
    private var clicked = false

    val glyphLayout = GlyphLayout().apply {
        setText(Fonts.font, text)
    }

    fun contains(x: Float, y: Float) = x > pos.x - size.x / 2
            && x < pos.x + size.x / 2
            && y > pos.y - size.y / 2
            && y < pos.y + size.y / 2

    fun hover(mouseX: Float, mouseY: Float) {
        hovered = contains(mouseX, mouseY)
    }

    fun click(mouseX: Float, mouseY: Float) {
        clicked = contains(mouseX, mouseY)
        if (clicked) down()
    }

    abstract fun down()

    fun release(mouseX: Float, mouseY: Float) {
        if (clicked && contains(mouseX, mouseY)) {
            clicked = false
            release()
        } else {
            clicked = false
        }
    }

    abstract fun release()

    fun draw(batch: Batch) {
        val font = if (hovered) Fonts.fontItalic else Fonts.font
        font.draw(batch, text, pos.x - glyphLayout.width / 2, pos.y + glyphLayout.height / 2)
    }

    fun drawBorder(shapeRenderer: ShapeRenderer) {
        if (hovered || clicked) {
            if (clicked) shapeRenderer.color = Color.CORAL
            shapeRenderer.rect(origin.x, origin.y, size.x, WIDTH)
            shapeRenderer.rect(origin.x, origin.y, WIDTH, size.y)
            shapeRenderer.rect(origin.x + size.x - WIDTH, origin.y, WIDTH, size.y)
            shapeRenderer.rect(origin.x, origin.y + size.y - WIDTH, size.x, WIDTH)
        }
    }

    fun drawBackground(shapeRenderer: ShapeRenderer) {
        shapeRenderer.color = if (clicked) Color.DARK_GRAY else Color.BLACK
        shapeRenderer.rect(origin.x , origin.y, size.x, size.y)
    }

}