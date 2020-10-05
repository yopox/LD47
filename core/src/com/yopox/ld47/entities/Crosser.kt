package com.yopox.ld47.entities

import com.yopox.ld47.LD47
import com.yopox.ld47.Levels
import com.yopox.ld47.screens.Screen
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class Crosser: Orbital(Levels.selected.enemy) {

    init {
        angle = LD47.random.nextFloat() * 2 * PI
        radius = Screen.WIDTH * 2
        acceleration = 10 * Levels.selected.minSpeed
        x = Screen.WIDTH / 2 + cos(angle).toFloat() * radius
        y = Screen.HEIGHT / 2 + sin(angle).toFloat() * radius

        angle = atan2(Screen.HEIGHT / 2 - y, Screen.WIDTH / 2 - x).toDouble()
        rotation = (angle / PI).toFloat() * 180 - 180
    }

    override fun update() {
        applyAcceleration()
        updateInvulnerability()

        x += speed * cos(angle).toFloat()
        y += speed * sin(angle).toFloat()

        if (speed >= 3 * Levels.selected.minSpeed) toDestroy = true
    }

    override fun hit(collision: Companion.Collision, otherOrbital: Orbital?) {
        triggerHit()
    }

}