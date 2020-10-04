package com.yopox.ld47.entities

import com.yopox.ld47.Assets
import com.yopox.ld47.LD47
import com.yopox.ld47.Resources

class Player : Orbital(LD47.assetManager[Assets.sprites[Resources.CAR1]]) {

    init {
        setOriginCenter()
    }

}