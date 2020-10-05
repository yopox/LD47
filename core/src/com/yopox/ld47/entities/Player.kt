package com.yopox.ld47.entities

import com.yopox.ld47.Resources

class Player : Orbital(Resources.CAR1) {

    init {
        setOriginCenter()
    }

    override fun hit(collision: Companion.Collision, otherOrbital: Orbital?) {
        when (collision) {
            Companion.Collision.NONE -> Unit
            Companion.Collision.FRONT_FRONT -> triggerHit()
            Companion.Collision.FRONT_BACK -> {
                triggerHit(); otherOrbital?.triggerHit()
            }
            Companion.Collision.BACK_FRONT -> triggerHit()
            Companion.Collision.BACK_BACK -> {
                triggerHit(); otherOrbital?.triggerHit()
            }
        }
    }

}