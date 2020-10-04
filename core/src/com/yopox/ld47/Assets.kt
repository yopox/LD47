package com.yopox.ld47

enum class Resources {
    OST_TITLE,
    OST_VROUM,
    CAR1,
    CAR2,
    CAR3,
    CAR4,
    BACKGROUND
}

object Assets {

    val sprites = mapOf(
            Resources.CAR1 to "sprites/car1.png",
            Resources.CAR2 to "sprites/car2.png",
            Resources.CAR3 to "sprites/car3.png",
            Resources.CAR4 to "sprites/car4.png",
            Resources.BACKGROUND to "circuit/global.png"
    )

    val sounds = mapOf(
            Resources.OST_TITLE to "OST/0 - écran titre.mp3",
            Resources.OST_VROUM to "OST/1 - Vroum.mp3"
    )

}