package ru.spb.mit.roboroguelike.attributes

import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.event.ChangeEvent
import org.hexworks.cobalt.databinding.api.event.ChangeListener
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component

/**
 * This attribute describes primary stats, essentially, attack and defence skills.
 * Applicable for player and for mobs. Influences the damage deal (attack) and damage absorbtion (defence)
 */
class EntityPrimaryStats(attackInitial: Int, defenceInitial: Int) : DisplayableAttribute, ProgressiveAttribute {
    private val attackProperty = createPropertyFrom(attackInitial)
    private val defenceProperty = createPropertyFrom(defenceInitial)
    var attack: Int by attackProperty.asDelegate()
    var defence: Int by defenceProperty.asDelegate()


    override fun onLevelUp(newLvl: Int) {
        if (newLvl % 2 == 0) {
            attack++
        } else {
            defence++
        }
    }

    override fun toComponent(width: Int): Component = Components.panel()
            .withSize(width, 3)
            .build().apply {

                val attackLabel = Components.label()
                        .withSize(width, 1)
                        .withPosition(0, 0)
                        .build()

                val defenceLabel = Components.label()
                        .withSize(width, 1)
                        .withPosition(0, 1)
                        .build()

                fun update() {
                    attackLabel.text = "Attack:$attack"
                    defenceLabel.text = "Defence:$defence"
                }

                val changeEventListener: ChangeListener<Int> = object : ChangeListener<Int> {
                    override fun onChange(changeEvent: ChangeEvent<Int>) {
                        update()
                    }
                }

                attackProperty.onChange(changeEventListener)
                defenceProperty.onChange(changeEventListener)
                update()

                addComponent(attackLabel)
                addComponent(defenceLabel)
            }
}