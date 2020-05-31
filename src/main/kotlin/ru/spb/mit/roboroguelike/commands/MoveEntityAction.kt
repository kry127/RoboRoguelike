package ru.spb.mit.roboroguelike.commands

import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.entities.GameEntity

data class MoveEntityAction(override val context: GameContext,
                            override val source: GameEntity<EntityType>,
                            override val target: GameEntity<EntityType>,
                            val position: Position3D): Action<EntityType>