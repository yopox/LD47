package com.yopox.ld47.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.yopox.ld47.screens.Screen
import kotlin.math.*

open class Orbital(texture: Texture) : Sprite(texture) {
    private var angle = 90f
    private var radius = CENTER
    private var leftOrbit = true
    private var speed = 2f


    private var angleDiff = 0f
    var facing = Facing.FRONT

    companion object {
        enum class Facing {
            FRONT, LEFT, RIGHT
        }
        val ANGLE_LIMIT = 16f
        val ANGLE_SPEED = 4f
        val LATERAL_SPEED = 3f

        val LEFT_FOCAL = Vector2(426f, Screen.HEIGHT / 2)
        val RIGHT_FOCAL = Vector2(848f, Screen.HEIGHT / 2)

        val CENTER = (RIGHT_FOCAL.x - LEFT_FOCAL.x) / 2
        val RADIUS_WIDTH = 100f
    }

    fun update() {
        when (facing) {
            Facing.FRONT -> faceFront()
            Facing.LEFT -> faceLeft()
            Facing.RIGHT -> faceRight()
        }

        angle += (if (leftOrbit) +1 else -1) * CENTER / radius * speed
        if (angle >= 360 || angle <= -180) {
            angle = if (leftOrbit) 180f else 0f
            leftOrbit = !leftOrbit
            radius = RIGHT_FOCAL.x - LEFT_FOCAL.x - radius
        }
        angle %= 360

        val dx = radius * cos(angle / 180 * PI).toFloat()
        val dy = radius * sin(angle / 180 * PI).toFloat()
        val origin = if (leftOrbit) LEFT_FOCAL else RIGHT_FOCAL

        this.setPosition(origin.x + dx - width / 2, origin.y + dy - height / 2)
        this.rotation = 90 + angle + angleDiff
    }

    fun faceRight() {
        angleDiff = max(angleDiff - ANGLE_SPEED, -ANGLE_LIMIT)
        val radiusDiff = if (leftOrbit) LATERAL_SPEED else -LATERAL_SPEED
        radius = min(CENTER + RADIUS_WIDTH, max(CENTER - RADIUS_WIDTH, radius + radiusDiff))
    }

    fun faceLeft() {
        angleDiff = min(angleDiff + ANGLE_SPEED, ANGLE_LIMIT)
        val radiusDiff = if (leftOrbit) LATERAL_SPEED else -LATERAL_SPEED
        radius = min(CENTER + RADIUS_WIDTH, max(CENTER - RADIUS_WIDTH, radius - radiusDiff))
        println("$radius")

    }

    fun faceFront() {
        if (angleDiff < 0) angleDiff = min(angleDiff + ANGLE_SPEED, 0f)
        if (angleDiff > 0) angleDiff = max(angleDiff - ANGLE_SPEED, 0f)
        if (abs(angleDiff) < ANGLE_SPEED) angleDiff = 0f
    }

}