package com.yopox.ld47.entities

import com.yopox.ld47.Levels
import com.yopox.ld47.Resources
import com.yopox.ld47.SoundManager

class Player : Orbital(Levels.selected.car) {

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
        SoundManager.sfx(Resources.SFX_HIT)
    }

    fun nitro() {
        if (acceleration < ACCELERATION_STEP) {
            nitro -= NITRO_COST
            acceleration = 1f
            SoundManager.sfx(Resources.SFX_NITRO)
        }
    }

    fun brake() {
        if (speed > 3f && (acceleration > 0 && acceleration < ACCELERATION_STEP || acceleration < 0 && acceleration > -ACCELERATION_STEP)) {
            nitro -= BRAKE_COST
            acceleration = -0.5f
        }
    }

}