package com.yopox.ld47.entities

import com.yopox.ld47.Levels
import com.yopox.ld47.Resources
import com.yopox.ld47.SoundManager
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

class Player : Orbital(Levels.selected.car) {

    private var nitro = 100f
    var nitroCounter = 0f
    private val NITRO_COST = 10f
    private val BRAKE_COST = 5f
    private val HIT_COST = 20f

    init {
        setOriginCenter()
        radius += 50f

        val dx = radius * cos(angle).toFloat()
        val dy = radius * sin(angle).toFloat()
        val origin = if (leftOrbit) LEFT_FOCAL else RIGHT_FOCAL
        this.setPosition(origin.x + dx - width / 2, origin.y + dy - height / 2)
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
                otherOrbital?.triggerHit()
                if (acceleration < ACCELERATION_STEP) triggerHit()
            }
            Companion.Collision.BACK_FRONT -> triggerHit()
            Companion.Collision.BACK_BACK -> {
                otherOrbital?.triggerHit()
                if (acceleration < ACCELERATION_STEP) triggerHit()
            }
        }
    }

    override fun triggerHit() {
        super.triggerHit()
        nitro -= HIT_COST
        val speedLoss = speed / 2
        speed -= speedLoss
        acceleration += max(Levels.selected.minSpeed - speed, 0f)
        SoundManager.sfx(Resources.SFX_HIT)
    }

    fun nitro() {
        if (acceleration < ACCELERATION_STEP && nitro >= NITRO_COST) {
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