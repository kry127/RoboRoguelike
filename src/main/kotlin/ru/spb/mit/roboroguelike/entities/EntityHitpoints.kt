package ru.spb.mit.roboroguelike.entities

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cavesofzircon.attributes.DisplayableAttribute
import org.hexworks.cobalt.databinding.api.converter.Converter
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.expression.concat
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.ComponentAlignment

class EntityHitpoints(val maxHp: Int, hpInitial : Int) : DisplayableAttribute {
    private val hpProperty = createPropertyFrom(hpInitial)
    var hp : Int by hpProperty.asDelegate()


    override fun toComponent(width: Int) : Component = Components.panel()
            .withSize(width, 2)
            .build().apply {

                val header = Components.textBox()
                        .withContentWidth(width)
                        .withAlignmentWithin(this, ComponentAlignment.TOP_CENTER)
                        .addHeader(" = Status =")
                        .build()

                val hpLabel = Components.label()
                        .withSize(width, 1)
                        .withAlignmentAround(header, ComponentAlignment.BOTTOM_CENTER)
                        .build()

//                hpLabel.textProperty.bind(createPropertyFrom("HP:  ")
//                        .concat(hpProperty)
//                        .concat("/").concat(maxHp))
                hpLabel.textProperty.bind(hpProperty, object : Converter<Int, String> {
                    override fun convert(source: Int): String {
                        return "HP:" + source.toString() + "/" + maxHp;
                    }

                })

                addComponent(header)
                addComponent(hpLabel)
            }
}