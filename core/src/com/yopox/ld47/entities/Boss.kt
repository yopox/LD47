package com.yopox.ld47.entities

import com.yopox.ld47.Levels
import com.yopox.ld47.Resources
import kotlin.math.PI

class Boss : Orbital(Levels.selected.boss) {

    var willCross = false
    var justUpdated = false

    init {
        setOriginCenter()
        forward = false
        radius -= 20f
        angle = PI
        speed = 2f
        rotation = (angle / PI * 180).toFloat() + rotationCorrection()
    }

    override fun shouldCross(): Boolean = willCross

    override fun update() {
        super.update()

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
    }

}