package com.yopox.ld47.graphics

import com.badlogic.gdx.math.Vector2
import com.yopox.ld47.SFX
import com.yopox.ld47.SoundManager

class MenuButton(text: String, pos: Vector2, private val callback: () -> Unit) : Button(text, pos, Vector2(192f, 80f)) {

    override fun down() {}

    override fun release() {
        callback()
        SoundManager.sfx(SFX.BUTTON)
    }
}