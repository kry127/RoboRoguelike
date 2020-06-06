package ru.spb.mit.roboroguelike.commands

import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D
import ru.spb.mit.roboroguelike.entities.GameCommand
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.entities.GameEntity
import ru.spb.mit.roboroguelike.entities.HealthBox

data class Consume(override val context: GameContext,
                   override val source: GameEntity<HealthBox>,
                   val position: Position3D) : GameCommand<EntityType>