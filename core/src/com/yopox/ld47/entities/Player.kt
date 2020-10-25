package com.yopox.ld47.entities

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.yopox.ld47.Levels
import com.yopox.ld47.Resources
import com.yopox.ld47.SoundManager
import kotlin.math.max
import kotlin.math.min

class Player(texture: Texture) : Orbital(texture) {

    var nitroCounter = 0f

    private val NITRO_MAX = 100f
    private var nitro = NITRO_MAX

    private val NITRO_COST = 10f
    private val BRAKE_COST = 5f
    private val HIT_COST = 20f
    private val NITRO_REFILL = 30f

    private val MAX_RADIUS = 310f
    private var tick = 0

    init {
        setOriginCenter()
        radius += 50f
        setCircularPosition()
    }

    override fun update() {
        tick %= 4
        super.update()

        when {
            nitroCounter < nitro -> nitroCounter += 1
            nitroCounter > nitro -> nitroCounter -= 1
        }

        if (radius > MAX_RADIUS && tick == 0) nitro -= 1f
    }

    override fun hit(collision: Companion.Collision, otherOrbital: Orbital?) {
        when (collision) {
            Companion.Collision.NONE -> Unit
            Companion.Collision.FRONT_FRONT -> triggerHit()
            Companion.Collision.FRONT_BACK -> {
                otherOrbital?.hit(Companion.Collision.FRONT_BACK.invert)
                if (acceleration < ACCELERATION_STEP) triggerHit()
            }
            Companion.Collision.BACK_FRONT -> triggerHit()
            Companion.Collision.BACK_BACK -> {
                otherOrbital?.hit(Companion.Collision.BACK_BACK.invert)
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
    }

    fun nitro(assetManager: AssetManager) {
        if (acceleration < ACCELERATION_STEP && nitro >= NITRO_COST) {
            nitro -= NITRO_COST
            acceleration = 1f
            SoundManager.sfx(assetManager, Resources.SFX_NITRO)
        }
    }

    fun brake() {
        if (speed > 3f && (acceleration > 0 && acceleration < ACCELERATION_STEP || acceleration < 0 && acceleration > -ACCELERATION_STEP)) {
            nitro -= BRAKE_COST
            acceleration = -0.5f
        }
    }

    fun collect(bonus: Bonus) {
        when (bonus.type) {
            Resources.MALUS -> acceleration -= 0.5f
            Resources.BONUS_NITRO -> nitro = min(NITRO_MAX, nitro + NITRO_REFILL)
            Resources.BONUS_BOOST -> acceleration += 0.5f
            else -> Unit
        }
        bonus.toDestroy = true
    }

}