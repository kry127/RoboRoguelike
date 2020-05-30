package ru.spb.mit.roboroguelike.commands

import org.hexworks.amethyst.api.entity.EntityType
import ru.spb.mit.roboroguelike.GameCommand
import ru.spb.mit.roboroguelike.GameEntity

interface Action<T: EntityType>: GameCommand<EntityType> {
    val target: GameEntity<T>
}