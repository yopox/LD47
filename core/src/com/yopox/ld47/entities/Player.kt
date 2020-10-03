package com.yopox.ld47.entities

import com.yopox.ld47.LD47

class Player : Orbital(LD47.assetManager["sprites/car.png"]) {

    init {
        setOriginCenter()
    }

}