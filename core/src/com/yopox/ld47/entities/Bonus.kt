package com.yopox.ld47.entities

import com.yopox.ld47.LD47
import com.yopox.ld47.Resources
import kotlin.math.PI

class Bonus(val type: Resources = kinds.random()): Orbital(type) {

    companion object {
        val kinds = arrayOf(Resources.BONUS_BOOST, Resources.BONUS_NITRO, Resources.MALUS)
    }

    init {
        leftOrbit = LD47.random.nextBoolean()
        angle = LD47.random.nextFloat() * PI * 2
        radius = RADIUS_MIN + LD47.random.nextFloat() * 150f
        setCircularPosition()
    }

}