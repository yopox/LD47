package com.yopox.ld47.entities

import com.yopox.ld47.Levels
import com.yopox.ld47.screens.Screen
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Boss : Orbital(Levels.selected.enemy) {

    var willCross = false
    var justUpdated = false
    private var life = 1

    init {
        setOriginCenter()
        forward = false
        radius -= 20f
        angle = PI
        speed = 2f
        rotation = (angle / PI * 180).toFloat() + rotationCorrection()
        setCircularPosition()
    }

    override fun shouldCross(): Boolean = willCross

    override fun update() {
        if (life > 0)
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
        life -= 1
        triggerHit()
        if (life == 0) acceleration += 5f
    }

}