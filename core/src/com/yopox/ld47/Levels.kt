package com.yopox.ld47

import com.badlogic.gdx.Gdx
import com.yopox.ld47.screens.LevelSelection

abstract class Level {

    companion object {
        const val PREFS = "scores"
    }

    abstract val name: String
    abstract val carName: String
    abstract val description: String
    abstract val car: Resources
    abstract val enemy: Resources
    abstract val background: Resources
    abstract val gameOver: Resources
    abstract val minSpeed: Float
    abstract val tick: Int

    var high: String
        get() = Gdx.app.getPreferences(PREFS).getString(name)
        set(value) {
            val prefs = Gdx.app.getPreferences(PREFS)
            prefs.putString(name, value)
            prefs.flush()
        }

}

object Levels {
    private val level1 = object : Level() {
        override val name: String = "P. Pastis Circuit"
        override val carName: String = "LANCIO DELTO FH"
        override val description: String = "Famous red and blue\ncircuit located in south\nFrance near Le Castellet."
        override val car: Resources = Resources.CAR1
        override val enemy: Resources = Resources.CAR2
        override val background: Resources = Resources.BACKGROUND1
        override val gameOver: Resources = Resources.GAMEOVER1
        override val minSpeed: Float = 4f
        override val tick: Int = 100
    }

    private val level2 = object : Level() {
        override val name: String = "Autodromo Ferraro"
        override val carName: String = "FERRARO 40F"
        override val description: String = "Remarkable circuit located\nin Bologna where you can\nsee lots of red Ferraros."
        override val car: Resources = Resources.CAR3
        override val enemy: Resources = Resources.CAR4
        override val background: Resources = Resources.BACKGROUND2
        override val gameOver: Resources = Resources.GAMEOVER2
        override val minSpeed: Float = 5f
        override val tick: Int = 75
    }

    private val level3 = object : Level() {
        override val name: String = "Woodgood Circuit"
        override val carName: String = "PUMA KX"
        override val description: String = "Great circuit near\nwest Sussex known for\nit's oldtimer racers."
        override val car: Resources = Resources.CAR5
        override val enemy: Resources = Resources.CAR6
        override val background: Resources = Resources.BACKGROUND3
        override val gameOver: Resources = Resources.GAMEOVER3
        override val minSpeed: Float = 7f
        override val tick: Int = 50
    }

    val levels = arrayOf(level1, level2, level3)

    val selected: Level
        get() = levels[LevelSelection.selected]
}