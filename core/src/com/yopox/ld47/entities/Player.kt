package com.yopox.ld47.entities

import com.yopox.ld47.Resources

class Player : Orbital(Resources.CAR1) {

    private var hit = false
    private var invulnerabilityFrames = 0

    init {
        setOriginCenter()
    }

    fun hit() {
        if (!hit) {
            hit = true
            invulnerabilityFrames = 60
        }
    }

    override fun update() {
        super.update()

        if (hit) {
            invulnerabilityFrames -= 1
            this.setAlpha(if ((invulnerabilityFrames / 5) % 2 == 0) 1f else 0f)
            if (invulnerabilityFrames == 0) hit = false
        }
    }

}