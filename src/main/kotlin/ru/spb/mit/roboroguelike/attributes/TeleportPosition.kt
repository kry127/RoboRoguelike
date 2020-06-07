package ru.spb.mit.roboroguelike.attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cavesofzircon.attributes.DisplayableAttribute
import org.hexworks.cobalt.databinding.api.converter.Converter
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.data.impl.Position3D

class TeleportPosition(teleportPosition: Position3D = Position3D.defaultPosition()) : DisplayableAttribute {
    private val positionProperty = createPropertyFrom(teleportPosition)

    var teleportPosition: Position3D by positionProperty.asDelegate()


    override fun toComponent(width: Int) : Component = Components.panel()
            .withSize(width, 4)
            .build().apply {


                val headerLabel = Components.label()
                        .withSize(width, 1)
                        .withText("Next teleport:")
                        .withPosition(0, 0)
                        .build()

                val positionLabelX = Components.label()
                        .withSize(width, 1)
                        .withPosition(0, 1)
                        .build()

                val positionLabelY = Components.label()
                        .withSize(width, 1)
                        .withPosition(0, 2)
                        .build()

                val positionLabelZ = Components.label()
                        .withSize(width, 1)
                        .withPosition(0, 3)
                        .build()



                positionLabelX.textProperty.bind(positionProperty, object : Converter<Position3D, String> {
                    override fun convert(source: Position3D): String {
                        return "     x=" + source.x;
                    }

                })

                positionLabelY.textProperty.bind(positionProperty, object : Converter<Position3D, String> {
                    override fun convert(source: Position3D): String {
                        return "     y=" + source.y;
                    }

                })

                positionLabelZ.textProperty.bind(positionProperty, object : Converter<Position3D, String> {
                    override fun convert(source: Position3D): String {
                        return "     z=" + source.z;
                    }

                })

                addComponent(headerLabel)
                addComponent(positionLabelX)
                addComponent(positionLabelY)
                addComponent(positionLabelZ)
            }
}
