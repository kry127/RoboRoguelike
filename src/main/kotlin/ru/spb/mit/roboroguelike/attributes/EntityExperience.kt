package ru.spb.mit.roboroguelike.attributes

import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.event.ChangeEvent
import org.hexworks.cobalt.databinding.api.event.ChangeListener
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import java.lang.Math.floor
import java.lang.Math.sqrt

class EntityExperience(xpInitial : Int) : DisplayableAttribute {
    private val xpProperty = createPropertyFrom(xpInitial)
    private var lvl : Int = xpToLevel(xpInitial)

    var xp : Int by xpProperty.asDelegate()

    companion object {
        fun levelToXp(lvl : Int) : Int = 100 * lvl * (lvl - 1)

        fun xpToLevel(xp : Int) : Int {
            // 200, 400, 600, 800, ...
            return floor(0.5 + 0.1*sqrt((25 + xp).toDouble())).toInt()
        }
    }

    private var onLevelUpListener : (Int) -> Unit = { }

    fun setOnLevelUpListener(changeListener : (Int) -> Unit) {
        onLevelUpListener = changeListener
    }

    private fun levelUp() {
        lvl++;
        onLevelUpListener(lvl)
    }


    override fun toComponent(width: Int) : Component =Components.panel()
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
                    levelLabel.text = "Lvl:" + currentLevel.toString();
                    xpLabel.text = "Exp:" + xp.toString();
                    nextLvlXp.text = "Next lvl: " + xpOfNextLevel;
                }

                val changeEventListener : ChangeListener<Int> = object : ChangeListener<Int> {
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