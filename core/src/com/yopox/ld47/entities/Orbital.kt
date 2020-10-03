package com.yopox.ld47.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.yopox.ld47.screens.Screen
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

open class Orbital(texture: Texture) : Sprite(texture) {
    private var angle = 90f
    private var radius = 200f
    private var leftOrbit = true
    private var speed = 1.5f

    companion object {
        val LEFT_FOCAL = Vector2(Screen.WIDTH / 3, Screen.HEIGHT / 2)
        val RIGHT_FOCAL = Vector2(Screen.WIDTH * 2 / 3, Screen.HEIGHT / 2)
    }

    fun update() {
        angle += if (leftOrbit) speed else -speed
        if (angle >= 360 || angle <= -180) {
            angle = if (leftOrbit) 180f else 0f
            leftOrbit = !leftOrbit
            radius = RIGHT_FOCAL.x - LEFT_FOCAL.x - radius
        }
        angle %= 360

        val dx = radius * cos(angle / 180 * PI).toFloat() - width / 2
        val dy = radius * sin(angle / 180 * PI).toFloat() - height / 2
        val origin = if (leftOrbit) LEFT_FOCAL else RIGHT_FOCAL

        this.setPosition(origin.x + dx, origin.y + dy)
        this.rotation = 90 + angle
    }

}