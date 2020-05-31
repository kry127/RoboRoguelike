package ru.spb.mit.roboroguelike.entities

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.zircon.api.data.impl.Position3D

class TeleportPosition(teleportPosition: Position3D = Position3D.defaultPosition()) : Attribute {
    private val positionProperty = createPropertyFrom(teleportPosition)

    var teleportPosition: Position3D by positionProperty.asDelegate()
}
