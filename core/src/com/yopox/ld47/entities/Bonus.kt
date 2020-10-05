package com.yopox.ld47.entities

import com.yopox.ld47.LD47
import com.yopox.ld47.Resources
import kotlin.math.PI

class Bonus(val type: Resources = kinds.random()): Orbital(type) {

    var tick = 0

    companion object {
        val kinds = arrayOf(Resources.BONUS_BOOST, Resources.BONUS_NITRO, Resources.MALUS)
    }

    init {
        leftOrbit = LD47.random.nextBoolean()
        angle = LD47.random.nextFloat() * PI * 2
        radius = RADIUS_MIN + LD47.random.nextFloat() * 150f
        setCircularPosition()
    }

    override fun update() {
        tick += 1
        if (tick == 500) toDestroy = true
    }

}