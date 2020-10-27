package com.yopox.ld47

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Texture

enum class Resources {
    CAR1,
    CAR2,
    CAR3,
    CAR4,
    CAR5,
    CAR6,
    CAR_BOSS,
    BACKGROUND1,
    GAMEOVER1,
    BACKGROUND2,
    GAMEOVER2,
    BACKGROUND3,
    GAMEOVER3,
    BONUS_NITRO,
    BONUS_BOOST,
    MALUS,
    GUI_BG,
    GUI_BG2,
    TITLE_BG,
    TITLE_TITLE,
    TITLE_SUB,
    TITLE_CAR1,
    TITLE_CAR2,
    TITLE_CAR3,
    ;

    companion object {
        val cars: Array<Resources>
            get() = arrayOf(
                    CAR1,
                    CAR2,
                    CAR3,
                    CAR4,
                    CAR5,
                    CAR6,
            )
        val titleCars: Array<Resources>
            get() = arrayOf(
                    TITLE_CAR1,
                    TITLE_CAR2,
                    TITLE_CAR3,
            )
        val bonuses: Array<Resources>
            get() = arrayOf(
                    BONUS_BOOST,
                    BONUS_NITRO,
                    MALUS,
            )
        val circuits: Array<Resources>
            get() = arrayOf(
                    BACKGROUND1,
                    BACKGROUND2,
                    BACKGROUND3,
            )
    }

}

enum class BGM {
    TITLE,
    GAME_OVER,
    LEVEL_ALT,
    LEVEL,
    BOSS,
    BOSS_ALT,
}

enum class SFX {
    BOSS,
    BUTTON,
    HIT,
    SELECT,
    START,
    GAME_OVER,
    NITRO,
    PAUSE,
    UNPAUSE,
}


object Assets {

    val sprites = mapOf(
            Resources.CAR1 to "cars/car1.png",
            Resources.CAR2 to "cars/car2.png",
            Resources.CAR3 to "cars/car3.png",
            Resources.CAR4 to "cars/car4.png",
            Resources.CAR5 to "cars/car5.png",
            Resources.CAR6 to "cars/car6.png",
            Resources.CAR_BOSS to "cars/car7-26.png",
            Resources.BACKGROUND1 to "circuit/global1.png",
            Resources.GAMEOVER1 to "gameover/gameover1.png",
            Resources.BACKGROUND2 to "circuit/global2.png",
            Resources.GAMEOVER2 to "gameover/gameover2.png",
            Resources.BACKGROUND3 to "circuit/global3-03.png",
            Resources.GAMEOVER3 to "gameover/gameover3.png",
            Resources.BONUS_BOOST to "bonus/bonusB.png",
            Resources.BONUS_NITRO to "bonus/bonusN.png",
            Resources.MALUS to "bonus/malus.png",
            Resources.GUI_BG to "gui_bg.png",
            Resources.GUI_BG2 to "gui_bg2.png",
            Resources.TITLE_BG to "titlescreen/background.png",
            Resources.TITLE_TITLE to "titlescreen/title.png",
            Resources.TITLE_SUB to "titlescreen/subtitle.png",
            Resources.TITLE_CAR1 to "titlescreen/cartitle1.png",
            Resources.TITLE_CAR2 to "titlescreen/cartitle2.png",
            Resources.TITLE_CAR3 to "titlescreen/cartitle3.png",
    )

    val bgms = mapOf(
            BGM.TITLE to "ost/0.ogg",
            BGM.GAME_OVER to "ost/5.ogg",
            BGM.LEVEL to "ost/1.ogg",
            BGM.LEVEL_ALT to "ost/2.ogg",
            BGM.BOSS to "ost/3.ogg",
            BGM.BOSS_ALT to "ost/4.ogg",
    )

    val sfxs = mapOf(
            SFX.BOSS to "sfx/boss_alert.ogg",
            SFX.BUTTON to "sfx/button.ogg",
            SFX.HIT to "sfx/collision.ogg",
            SFX.SELECT to "sfx/confirm.ogg",
            SFX.START to "sfx/go.ogg",
            SFX.GAME_OVER to "sfx/mort.ogg",
            SFX.NITRO to "sfx/nitro.ogg",
            SFX.PAUSE to "sfx/pause.ogg",
            SFX.UNPAUSE to "sfx/unpause.ogg",
    )

    fun load(assetManager: AssetManager, asset: Resources) = when (asset) {
        in sprites.keys -> assetManager.load(sprites[asset], Texture::class.java)
        else -> throw Exception("Unknown resource: $asset")
    }

}