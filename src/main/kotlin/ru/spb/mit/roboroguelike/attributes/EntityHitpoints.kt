package ru.spb.mit.roboroguelike.attributes

import org.hexworks.cobalt.databinding.api.converter.Converter
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component

/**
 * This class represents hitpoints of the entity: maxHp (handicap) and initial hitpoints.
 * For health kits and artifacts applicable [ maxHp == hpInitial == hp ]
 * For players and mobs this is not guaranteed
 */
class EntityHitpoints(var maxHp: Int, hpInitial: Int) : DisplayableAttribute {
    private val hpProperty = createPropertyFrom(hpInitial)
    var hp: Int by hpProperty.asDelegate()

    /**
     * This method describes HP dynamics during level progression
     */
    fun onLevelUp(newLvl: Int) {
        maxHp += 5
        hp = maxHp
    }

    override fun toComponent(width: Int): Component {

        val hpLabel = Components.label()
                .withSize(width, 2)
                .build()

        hpLabel.textProperty.bind(hpProperty, object : Converter<Int, String> {
            override fun convert(source: Int): String {
                return "HP:$source/$maxHp"
            }

        })

        return hpLabel
    }
}