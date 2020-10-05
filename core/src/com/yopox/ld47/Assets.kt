package com.yopox.ld47

enum class Resources {
    OST_TITLE,
    OST_GAME_OVER,
    OST_LEVEL_ALT,
    OST_LEVEL,
    OST_BOSS,
    OST_BOSS_ALT,
    SFX_BOSS,
    SFX_BUTTON,
    SFX_HIT,
    SFX_SELECT,
    SFX_321,
    SFX_GAME_OVER,
    SFX_NITRO,
    SFX_PAUSE,
    SFX_UNPAUSE,
    CAR1,
    CAR2,
    CAR3,
    CAR4,
    BACKGROUND,
    GUI_BG,
    GUI_BG2
}

object Assets {

    val sprites = mapOf(
            Resources.CAR1 to "sprites/car3.png",
            Resources.CAR2 to "sprites/car4.png",
            Resources.CAR3 to "sprites/car3.png",
            Resources.CAR4 to "sprites/car4.png",
            Resources.BACKGROUND to "circuit/global2.png",
            Resources.GUI_BG to "gui_bg.png",
            Resources.GUI_BG2 to "gui_bg2.png"
    )

    val bgms = mapOf(
            Resources.OST_TITLE to "ost/0.ogg",
            Resources.OST_GAME_OVER to "ost/5.ogg",
            Resources.OST_LEVEL to "ost/1.ogg",
            Resources.OST_LEVEL_ALT to "ost/2.ogg",
            Resources.OST_BOSS to "ost/3.ogg",
            Resources.OST_BOSS_ALT to "ost/4.ogg"
    )

    val sfxs = mapOf(
            Resources.SFX_BOSS to "sfx/boss_alert.ogg",
            Resources.SFX_BUTTON to "sfx/button.ogg",
            Resources.SFX_HIT to "sfx/collision.ogg",
            Resources.SFX_SELECT to "sfx/confirm.ogg",
            Resources.SFX_321 to "sfx/go.ogg",
            Resources.SFX_GAME_OVER to "sfx/mort.ogg",
            Resources.SFX_NITRO to "sfx/nitro.ogg",
            Resources.SFX_PAUSE to "sfx/pause.ogg",
            Resources.SFX_UNPAUSE to "sfx/unpause.ogg"
    )

}