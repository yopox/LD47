package com.yopox.ld47.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.yopox.ld47.Assets
import com.yopox.ld47.LD47
import com.yopox.ld47.Resources
import com.yopox.ld47.screens.Screen
import kotlin.math.*

open class Orbital(textureID: Resources) : Sprite(LD47.assetManager.get(Assets.sprites[textureID], Texture::class.java)) {
    internal var angle = PI / 4
    internal var radius = CENTER
    internal var leftOrbit = true
    internal var speed = 4f
    private var movement = Movement.CIRCULAR
    private var linearAngle = 0.0
    internal var forward = true

    var facing = Facing.FRONT

    companion object {
        enum class Movement {
            CIRCULAR, LINEAR
        }

        val MIN_CIRCULAR_ANGLE = PI / 4

        enum class Facing {
            FRONT, LEFT, RIGHT
        }

        val ANGLE_LIMIT = 10f
        val ANGLE_SPEED = 6f
        val LATERAL_SPEED = 4.5f

        val LEFT_FOCAL = Vector2(426f, Screen.HEIGHT / 2)
        val RIGHT_FOCAL = Vector2(848f, Screen.HEIGHT / 2)

        val RADIUS_MIN = 80f
        val CENTER = 145f
    }

    open fun update() {
        when (facing) {
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
                    if (shouldCross()) {
                        movement = Movement.LINEAR
                        linearAngle = it
                        val nextAngle = atan2(Screen.HEIGHT / 2 - orbitalY, Screen.WIDTH / 2 - orbitalX).toDouble() -
                                linearCorrection()
                        angle = nextAngle
                    }
                }
            }
            Movement.LINEAR -> {
                val dx = (speed * 2 * cos(linearAngle)).toFloat()
                val dy = (speed * 2 * sin(linearAngle)).toFloat()
                x += dx
                y += dy

                val targetFocal = if (leftOrbit) RIGHT_FOCAL else LEFT_FOCAL
                val focalRadius = targetFocal.dst(orbitalX, orbitalY)
                val previousRadius = targetFocal.dst(orbitalX - dx, orbitalY - dy)
                val nextRadius = targetFocal.dst(orbitalX + dx, orbitalY + dy)
                if (previousRadius > focalRadius && nextRadius > focalRadius) {
                    movement = Movement.CIRCULAR
                    leftOrbit = !leftOrbit
                    val nextAngle = acos((orbitalX - targetFocal.x) / focalRadius).toDouble().normalize +
                            if (!forward) if (!leftOrbit) PI / 2 else 3 * PI / 2 else 0.0
                    angle = nextAngle
                    radius = focalRadius
                }
            }
        }

        // Update rotation
        val oldRotation = (this.rotation - rotationCorrection() + 360f) % 360
        val targetAngle = ((angle.normalize / PI * 180).toFloat() +
                (if (facing == Facing.LEFT) ANGLE_LIMIT else 0f) +
                (if (facing == Facing.RIGHT) -ANGLE_LIMIT else 0f) + 360f * 2) % 360
        val diff = abs(oldRotation - targetAngle)
        val nextRotation = when {
            abs(oldRotation - targetAngle) < ANGLE_SPEED -> targetAngle
            oldRotation - targetAngle < 0 -> oldRotation + if (diff < 180) ANGLE_SPEED else -ANGLE_SPEED
            oldRotation - targetAngle > 0 -> oldRotation - if (diff < 180) ANGLE_SPEED else -ANGLE_SPEED
            else -> targetAngle
        }
        this.rotation = nextRotation + rotationCorrection()
    }

    open fun turn() {}

    open fun shouldCross(): Boolean = true

    internal fun rotationCorrection(): Float = (if (leftOrbit) -90f else 90f) * if (forward) 1 else -1

    internal fun linearCorrection(): Double = (if (leftOrbit) -3 * PI / 2 else 3 * PI / 2) * if (forward) 1 else -1

    fun faceRight() {
        when (movement) {
            Movement.CIRCULAR -> {
                val radiusDiff = if (leftOrbit) LATERAL_SPEED else -LATERAL_SPEED
                radius = max(RADIUS_MIN, radius + radiusDiff)
            }
            Movement.LINEAR -> {
                if (!leftOrbit || leftOrbit && RIGHT_FOCAL.dst(orbitalX, orbitalY) > 2 * RADIUS_MIN) {
                    x += LATERAL_SPEED * cos(linearAngle - PI / 2).toFloat()
                    y += LATERAL_SPEED * sin(linearAngle - PI / 2).toFloat()
                }
            }
        }
    }

    fun faceLeft() {
        when (movement) {
            Movement.CIRCULAR -> {
                val radiusDiff = if (leftOrbit) LATERAL_SPEED else -LATERAL_SPEED
                radius = max(RADIUS_MIN, radius - radiusDiff)
            }
            Movement.LINEAR -> {
                if (leftOrbit || !leftOrbit && LEFT_FOCAL.dst(orbitalX, orbitalY) > 2 * RADIUS_MIN) {
                    x += LATERAL_SPEED * cos(linearAngle + PI / 2).toFloat()
                    y += LATERAL_SPEED * sin(linearAngle + PI / 2).toFloat()
                }
            }
        }

    }

    private fun linearAngle(): Double? {
        return when {
            forward && leftOrbit && orbitalY < LEFT_FOCAL.y && angle.normalize > 2 * PI - MIN_CIRCULAR_ANGLE ->
                (MIN_CIRCULAR_ANGLE + (atan2(orbitalY - Screen.HEIGHT / 2, orbitalX - Screen.WIDTH / 2) - PI)).normalize / 2
            forward && !leftOrbit && orbitalY < RIGHT_FOCAL.y && angle.normalize < PI + MIN_CIRCULAR_ANGLE ->
                (PI - MIN_CIRCULAR_ANGLE + (atan2(orbitalY - Screen.HEIGHT / 2, orbitalX - Screen.WIDTH / 2) - PI)).normalize / 2
            !forward && leftOrbit && orbitalY > LEFT_FOCAL.y && angle.normalize < MIN_CIRCULAR_ANGLE ->
                (2 * PI - MIN_CIRCULAR_ANGLE + (atan2(orbitalY - Screen.HEIGHT / 2, orbitalX - Screen.WIDTH / 2))).normalize / 2 - PI / 2
            !forward && !leftOrbit && orbitalY > RIGHT_FOCAL.y && angle.normalize > PI - MIN_CIRCULAR_ANGLE ->
                (PI + MIN_CIRCULAR_ANGLE + (atan2(orbitalY - Screen.HEIGHT / 2, orbitalX - Screen.WIDTH / 2))).normalize / 2 + PI / 2
            else -> null
        }
    }

    private val Double.normalize: Double
        get() = (this + ceil(abs(this)) * 2 * PI) % (2 * PI)

    internal val orbitalX: Float
        get() = x + originX

    internal val orbitalY: Float
        get() = y + originY

}