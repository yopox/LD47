package com.yopox.ld47

import com.badlogic.gdx.graphics.Texture

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
    CAR5,
    CAR6,
    BACKGROUND1,
    GAMEOVER1,
    BACKGROUND2,
    GAMEOVER2,
    BACKGROUND3,
    GUI_BG,
    GUI_BG2
}

object Assets {

    val sprites = mapOf(
            Resources.CAR1 to "sprites/car1.png",
            Resources.CAR2 to "sprites/car2.png",
            Resources.CAR3 to "sprites/car3.png",
            Resources.CAR4 to "sprites/car4.png",
            Resources.CAR5 to "sprites/car5.png",
            Resources.CAR6 to "sprites/car6.png",
            Resources.BACKGROUND1 to "circuit/global1.png",
            Resources.GAMEOVER1 to "gameover/gameover1.png",
            Resources.BACKGROUND2 to "circuit/global2.png",
            Resources.GAMEOVER2 to "gameover/gameover2.png",
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

    fun getTexture(resource: Resources): Texture = LD47.assetManager.get(sprites[resource], Texture::class.java)

}