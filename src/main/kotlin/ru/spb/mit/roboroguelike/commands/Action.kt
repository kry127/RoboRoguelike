package ru.spb.mit.roboroguelike.commands

import org.hexworks.amethyst.api.entity.EntityType
import ru.spb.mit.roboroguelike.entities.GameCommand
import ru.spb.mit.roboroguelike.entities.GameEntity

interface Action<T: EntityType>: GameCommand<EntityType> {
    val target: GameEntity<T>
}