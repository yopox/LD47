package com.yopox.ld47.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.yopox.ld47.LD47
import com.yopox.ld47.screens.Screen

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.width = Screen.WIDTH.toInt()
        config.height = Screen.HEIGHT.toInt()
        config.title = "Infinite Racer"
        LwjglApplication(LD47(), config)
    }
}