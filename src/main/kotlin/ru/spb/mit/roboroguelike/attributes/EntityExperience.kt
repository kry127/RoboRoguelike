package ru.spb.mit.roboroguelike.attributes

import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.event.ChangeEvent
import org.hexworks.cobalt.databinding.api.event.ChangeListener
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import kotlin.math.floor
import kotlin.math.sqrt

/**
 * This attribute describes experience and level of player entity.
 * In addition, it describes level scaling depending on amount of experience.
 * Many characteristics of the player depends on the level: HP, attack, defence.
 * XP can be gained by killing mobs.
 */
class EntityExperience(xpInitial: Int) : DisplayableAttribute {
    private val xpProperty = createPropertyFrom(xpInitial)
    private var lvl: Int = xpToLevel(xpInitial)

    var xp: Int by xpProperty.asDelegate()

    companion object {

        /**
        The more level, the more experience you need to increase from
        current level to next level. Deltas of experience scaling:
        200, 400, 600, 800, ...
        Absolute values:
        0, 200, 600, 1200, 2000, 3000, ...

         */
        fun levelToXp(lvl: Int): Int = 100 * lvl * (lvl - 1)

        /**
        The more level, the more experience you need to increase from
        current level to next level. Deltas of experience scaling:
        200, 400, 600, 800, ...
        Absolute values:
        0, 200, 600, 1200, 2000, 3000, ...

         */
        fun xpToLevel(xp: Int): Int {
            return floor(0.5 + 0.1 * sqrt((25 + xp).toDouble())).toInt()
        }
    }

    private var onLevelUpListener: (Int) -> Unit = { }

    /**
     * Set up listener to subscribe on event of leveling up the player
     */
    fun setOnLevelUpListener(changeListener: (Int) -> Unit) {
        onLevelUpListener = changeListener
    }

    private fun levelUp() {
        lvl++
        onLevelUpListener(lvl)
    }


    override fun toComponent(width: Int): Component = Components.panel()
            .withSize(width, 4)
            .build().apply {

                val levelLabel = Components.label()
                        .withSize(width, 1)
                        .withPosition(0, 0)
                        .build()

                val xpLabel = Components.label()
                        .withSize(width, 1)
                        .withPosition(0, 1)
                        .build()
                val nextLvlXp = Components.label()
                        .withSize(width, 1)
                        .withPosition(0, 2)
                        .build()

                fun update() {
                    val currentLevel = xpToLevel(xp)
                    while (currentLevel > lvl) {
                        levelUp()
                    }
                    val xpOfNextLevel = levelToXp(currentLevel + 1)
                    levelLabel.text = "Lvl:$currentLevel"
                    xpLabel.text = "Exp:$xp"
                    nextLvlXp.text = "Next lvl:$xpOfNextLevel"
                }

                val changeEventListener: ChangeListener<Int> = object : ChangeListener<Int> {
                    override fun onChange(changeEvent: ChangeEvent<Int>) {
                        update()
                    }
                }

                xpProperty.onChange(changeEventListener)
                update()

                addComponent(levelLabel)
                addComponent(xpLabel)
                addComponent(nextLvlXp)
            }
}