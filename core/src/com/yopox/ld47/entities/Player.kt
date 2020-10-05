package com.yopox.ld47.entities

import com.yopox.ld47.Resources

class Player : Orbital(Resources.CAR1) {

    private var nitro = 100f
    var nitroCounter = 0f
    private val NITRO_COST = 25f
    private val BRAKE_COST = 10f
    private val HIT_COST = 30f

    init {
        setOriginCenter()
        radius += 50f
    }

    override fun update() {
        super.update()

        when {
            nitroCounter < nitro -> nitroCounter += 1
            nitroCounter > nitro -> nitroCounter -= 1
        }
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

    override fun triggerHit() {
        super.triggerHit()
        nitro -= HIT_COST
    }

    fun nitro() {
        nitro -= NITRO_COST
        acceleration = 1f
    }

    fun brake() {
        if (speed > 3f && (acceleration > 0 && acceleration < ACCELERATION_STEP || acceleration < 0 && acceleration > -ACCELERATION_STEP)) {
            nitro -= BRAKE_COST
            acceleration = -0.5f
        }
    }

}