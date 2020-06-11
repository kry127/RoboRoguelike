package ru.spb.mit.roboroguelike.attributes

import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.event.ChangeEvent
import org.hexworks.cobalt.databinding.api.event.ChangeListener
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component

/**
 * Эти атрибуты отвечают за имеющуюся у героя атаку и защиту. Возможно,
 * стоит продумать, как надеваемые шмотки будут влиять на характеристики игрока
 */
class EntityPrimaryStats(attackInitial : Int, defenceInitial : Int) : DisplayableAttribute {
    private val attackProperty = createPropertyFrom(attackInitial)
    private val defenceProperty = createPropertyFrom(defenceInitial)
    var attack : Int by attackProperty.asDelegate()
    var defence : Int by defenceProperty.asDelegate()


    override fun toComponent(width: Int) : Component = Components.panel()
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
                    attackLabel.text = "Attack:" + attack.toString();
                    defenceLabel.text = "Defence:" + defence.toString();
                }

                val changeEventListener : ChangeListener<Int> = object : ChangeListener<Int> {
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