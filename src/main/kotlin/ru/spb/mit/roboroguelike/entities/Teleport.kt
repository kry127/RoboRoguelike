package ru.spb.mit.roboroguelike.entities

import org.hexworks.amethyst.api.entity.EntityType
import ru.spb.mit.roboroguelike.attributes.TeleportPosition

interface Teleport: EntityType

val GameEntity<Teleport>.getTeleportPosition: TeleportPosition
    get() = findAttribute(TeleportPosition::class).get()
