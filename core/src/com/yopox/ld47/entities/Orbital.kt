package com.yopox.ld47.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.yopox.ld47.screens.Screen
import kotlin.math.*

open class Orbital(texture: Texture) : Sprite(texture) {
    private var angle = PI / 4
    private var radius = CENTER
    private var leftOrbit = true
    private var speed = 6f
    private var movement = Movement.CIRCULAR
    private var linearAngle = 0.0
    private var forward = true

    private var angleDiff = 0f
    var facing = Facing.FRONT

    companion object {
        enum class Movement {
            CIRCULAR, LINEAR
        }

        val MIN_CIRCULAR_ANGLE = PI / 4

        enum class Facing {
            FRONT, LEFT, RIGHT
        }

        val ANGLE_LIMIT = 12f
        val ANGLE_SPEED = 2f
        val LATERAL_SPEED = 3f

        val LEFT_FOCAL = Vector2(426f, Screen.HEIGHT / 2)
        val RIGHT_FOCAL = Vector2(848f, Screen.HEIGHT / 2)

        val RADIUS_MIN = 45f
        val RADIUS_MAX = 210f
        val CENTER = 145f
    }

    fun update() {
        when (facing) {
            Facing.FRONT -> faceFront()
            Facing.LEFT -> faceLeft()
            Facing.RIGHT -> faceRight()
        }

        when (movement) {
            Movement.CIRCULAR -> {
                // Angle update
                angle += (if (forward) +1 else -1) * (if (leftOrbit) +1 else -1) * CENTER / radius * speed / 180 * PI

                // Position update
                val dx = radius * cos(angle).toFloat()
                val dy = radius * sin(angle).toFloat()
                val origin = if (leftOrbit) LEFT_FOCAL else RIGHT_FOCAL
                this.setPosition(origin.x + dx - width / 2, origin.y + dy - height / 2)

                // Movement switching condition
                linearAngle()?.let {
                    movement = Movement.LINEAR
                    linearAngle = it
                }
            }
            Movement.LINEAR -> {
                val dx = (speed * 2 * cos(linearAngle)).toFloat()
                val dy = (speed * 2 * sin(linearAngle)).toFloat()
                x += dx
                y += dy

                val targetFocal = if (leftOrbit) RIGHT_FOCAL else LEFT_FOCAL
                val focalRadius = targetFocal.dst(orbitalX, orbitalY)
                if (targetFocal.dst(orbitalX - dx, orbitalY - dy) > focalRadius
                        && targetFocal.dst(orbitalX + dx, orbitalY + dy) > focalRadius) {
                    movement = Movement.CIRCULAR
                    leftOrbit = !leftOrbit
                    angle = atan2(orbitalY - targetFocal.y, orbitalX - targetFocal.x).toDouble().normalize
                    radius = focalRadius
                }
            }
        }

        this.rotation = (if (leftOrbit) -90 else 90) + (angle / PI * 180).toFloat() + angleDiff
    }

    fun faceRight() {
        when (movement) {
            Movement.CIRCULAR -> {
                angleDiff = max(angleDiff - ANGLE_SPEED, -ANGLE_LIMIT)
                val radiusDiff = if (leftOrbit) LATERAL_SPEED else -LATERAL_SPEED
                radius = max(RADIUS_MIN, radius + radiusDiff)
            }
            Movement.LINEAR -> {
                x += speed / 2 * cos(linearAngle - PI / 2).toFloat()
                y += speed / 2 * sin(linearAngle - PI / 2).toFloat()
            }
        }
    }

    fun faceLeft() {
        when (movement) {
            Movement.CIRCULAR -> {
                angleDiff = min(angleDiff + ANGLE_SPEED, ANGLE_LIMIT)
                val radiusDiff = if (leftOrbit) LATERAL_SPEED else -LATERAL_SPEED
                radius = max(RADIUS_MIN, radius - radiusDiff)
            }
            Movement.LINEAR -> {
                x += speed / 2 * cos(linearAngle + PI / 2).toFloat()
                y += speed / 2 * sin(linearAngle + PI / 2).toFloat()
            }
        }

    }

    fun faceFront() {
        if (angleDiff < 0) angleDiff = min(angleDiff + ANGLE_SPEED, 0f)
        if (angleDiff > 0) angleDiff = max(angleDiff - ANGLE_SPEED, 0f)
        if (abs(angleDiff) < ANGLE_SPEED) angleDiff = 0f
    }

    private fun linearAngle(): Double? {
        return when {
            forward && leftOrbit && angle > 2 * PI - MIN_CIRCULAR_ANGLE -> MIN_CIRCULAR_ANGLE
            forward && !leftOrbit && angle < -PI + MIN_CIRCULAR_ANGLE -> PI - MIN_CIRCULAR_ANGLE
            else -> null
        }
    }

    private val Double.normalize: Double
        get() = (this + 2 * PI) % (2 * PI)

    private val orbitalX: Float
        get() = x + originX

    private val orbitalY: Float
        get() = y + originY

}