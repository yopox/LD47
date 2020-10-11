package com.yopox.ld47.graphics

import com.badlogic.gdx.math.Vector2

class PadButton(text: String, pos: Vector2, private val downCallback: () -> Unit, private val upCallback: () -> Unit) : Button(text, pos, Vector2(SIZE, SIZE)) {

    companion object {
        val SIZE = 120f
    }

    override fun down() {
        downCallback()
    }

    override fun release() {
        upCallback()
    }

}