package ru.spb.mit.roboroguelike.attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.zircon.api.data.impl.Position3D

/**
 * This attribute describes teleportation jump. Essentialy used as a ladder between
 * two levels of the world.
 */
class TeleportPosition(teleportPosition: Position3D = Position3D.defaultPosition()) : Attribute {
    private val positionProperty = createPropertyFrom(teleportPosition)

    var teleportPosition: Position3D by positionProperty.asDelegate()

}
