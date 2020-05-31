package ru.spb.mit.roboroguelike.entities

import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D

interface Teleport: EntityType

val GameEntity<Teleport>.getTeleportPosition: TeleportPosition
    get() = findAttribute(TeleportPosition::class).get()
