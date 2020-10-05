package com.yopox.ld47

import com.yopox.ld47.screens.LevelSelection

abstract class Level {

    abstract val name: String
    abstract val carName: String
    abstract val description: String
    abstract val car: Resources
    abstract val boss: Resources
    abstract val background: Resources

}

object Levels {
    private val level1 = object : Level() {
        override val name: String = "Level One"
        override val carName: String = "SUPER CAR"
        override val description: String = "This level is super.\nLine 2 is super too!\nLine 3 is okay..."
        override val car: Resources = Resources.CAR1
        override val boss: Resources = Resources.CAR2
        override val background: Resources = Resources.BACKGROUND1
    }

    private val level2 = object : Level() {
        override val name: String = "Level Two"
        override val carName: String = "ALSO SUPER CAR"
        override val description: String = "This level more super.\nLine 2 is more super too!\nLine 3 is still okay..."
        override val car: Resources = Resources.CAR3
        override val boss: Resources = Resources.CAR4
        override val background: Resources = Resources.BACKGROUND2
    }

    val levels = arrayOf(level1, level2)

    val selected: Level
        get() = levels[LevelSelection.selected]
}