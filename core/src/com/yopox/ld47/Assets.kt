package com.yopox.ld47

enum class Resources {
    OST_TITLE,
    OST_VROUM,
    CAR1,
    CAR2,
    CAR3,
    CAR4,
    BACKGROUND,
    GUI_BG
}

object Assets {

    val sprites = mapOf(
            Resources.CAR1 to "sprites/car3.png",
            Resources.CAR2 to "sprites/car4.png",
            Resources.CAR3 to "sprites/car3.png",
            Resources.CAR4 to "sprites/car4.png",
            Resources.BACKGROUND to "circuit/global2.png",
            Resources.GUI_BG to "gui_bg.png"
    )

    val sounds = mapOf(
            Resources.OST_TITLE to "ost/0.ogg",
            Resources.OST_VROUM to "ost/1.ogg"
    )

}