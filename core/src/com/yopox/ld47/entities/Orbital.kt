package com.yopox.ld47.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.yopox.ld47.screens.Main
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

open class Orbital(texture: Texture) : Sprite(texture) {
    private var angle = 0f
    private var radius = 200f
    private var clockwise = true
    private var leftOrbit = true
    private var speed = 1f

    fun update() {
        angle += if (clockwise) speed else -speed
        angle %= 360

        val dx = radius * cos(angle / 180 * PI).toFloat() - width / 2
        val dy = radius * sin(angle / 180 * PI).toFloat() - height / 2
        val origin = if (leftOrbit) Main.LEFT_PLANET else Main.RIGHT_PLANET

        this.setPosition(origin.x + dx, origin.y + dy)
        this.rotation = 90 + angle
    }

}