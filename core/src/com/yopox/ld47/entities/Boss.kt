package com.yopox.ld47.entities

import com.badlogic.gdx.math.Vector2
import com.yopox.ld47.Levels
import com.yopox.ld47.screens.Screen
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Boss(var lives: Int = 3) : Orbital(Levels.selected.enemy) {

    var willCross = false
    var justUpdated = false

    private data class Starting(val leftOrbit: Boolean, val angle: Double, val pos: Vector2)

    init {
        setOriginCenter()
        forward = false

        movement = Companion.Movement.LINEAR

        radius = Screen.HEIGHT
        val startingPos = arrayOf(
                Starting(true, 4 * PI / 5, Vector2(
                        Screen.WIDTH / 2 + cos(4 * PI / 5).toFloat() * radius - 32f,
                        Screen.HEIGHT / 2 + sin(4 * PI / 5).toFloat() * radius
                )),
                Starting(false, PI / 5, Vector2(
                        Screen.WIDTH / 2 + cos(PI / 5).toFloat() * radius + 32f,
                        Screen.HEIGHT / 2 + sin(PI / 5).toFloat() * radius
                ))).random()

        leftOrbit = startingPos.leftOrbit

        acceleration = 1.5f * Levels.selected.minSpeed

        x = startingPos.pos.x
        y = startingPos.pos.y

        linearAngle = (startingPos.angle - PI).normalize
        angle = (linearAngle + if (leftOrbit) PI / 2 else -PI / 2).normalize
    }

    override fun shouldCross(): Boolean = willCross

    override fun update() {
        if (lives > 0)
            super.update()
        else {
            applyAcceleration()
            updateInvulnerability()

            x += speed * cos(angle + PI / 2).toFloat()
            y += speed * sin(angle + PI / 2).toFloat()

            if (x - width > Screen.WIDTH || x + width < 0 || y - width > Screen.HEIGHT || y + width < 0) {
                toDestroy = true
            }
        }

        if (leftOrbit && orbitalX < LEFT_FOCAL.x || !leftOrbit && orbitalX > RIGHT_FOCAL.x) {
            if (!justUpdated) {
                justUpdated = true
                willCross = !willCross
            }
        } else {
            justUpdated = false
        }
    }

    override fun hit(collision: Companion.Collision, otherOrbital: Orbital?) {
        lives -= 1
        triggerHit()
        if (lives == 0) acceleration += 5f
    }

}