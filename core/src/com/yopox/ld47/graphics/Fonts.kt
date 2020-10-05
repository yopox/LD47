package com.yopox.ld47.graphics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.math.Vector2

object Fonts {

    lateinit var font: BitmapFont
    lateinit var fontSmall: BitmapFont
    lateinit var fontItalic: BitmapFont
    lateinit var fontTitle: BitmapFont

    private fun bitmapFont(name: String, size: Int): BitmapFont {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("fonts/$name"))
        val font = generator.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().also { param ->
            param.size = size
        })
        generator.dispose()
        return font
    }

    fun genFonts() {
        font = bitmapFont("JetBrainsMono-Medium.ttf", 36)
        fontSmall = bitmapFont("JetBrainsMono-Medium.ttf", 28)
        fontTitle = bitmapFont("JetBrainsMono-Medium.ttf", 72)
        fontItalic = bitmapFont("JetBrainsMono-Medium-Italic.ttf", 36)
    }

    fun BitmapFont.drawCentered(batch: Batch, text: String, pos: Vector2, containerSize: Vector2) {
        val glyph = GlyphLayout().apply {
            setText(this@drawCentered, text)
        }
        draw(batch, text,
                pos.x + containerSize.x / 2 - glyph.width / 2,
                pos.y + containerSize.y / 2 + glyph.height / 2)
    }

}