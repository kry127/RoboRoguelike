package ru.spb.mit.roboroguelike.commands

import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D
import ru.spb.mit.roboroguelike.GameCommand
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.GameEntity

data class MoveTo(override val context: GameContext,
                      override val source: GameEntity<EntityType>,
                      val position: Position3D) : GameCommand<EntityType>