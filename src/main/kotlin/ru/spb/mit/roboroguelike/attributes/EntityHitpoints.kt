package ru.spb.mit.roboroguelike.attributes

import org.hexworks.cavesofzircon.attributes.DisplayableAttribute
import org.hexworks.cobalt.databinding.api.converter.Converter
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.ComponentAlignment

class EntityHitpoints(val maxHp: Int, hpInitial : Int) : DisplayableAttribute {
    private val hpProperty = createPropertyFrom(hpInitial)
    var hp : Int by hpProperty.asDelegate()


    override fun toComponent(width: Int) : Component {

                val hpLabel = Components.label()
                        .withSize(width, 2)
                        .build()

                hpLabel.textProperty.bind(hpProperty, object : Converter<Int, String> {
                    override fun convert(source: Int): String {
                        return "HP:" + source.toString() + "/" + maxHp;
                    }

                })

                return hpLabel
            }
}