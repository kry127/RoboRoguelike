package ru.spb.mit.roboroguelike.commands

import org.hexworks.amethyst.api.entity.EntityType
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.entities.GameCommand
import ru.spb.mit.roboroguelike.entities.GameEntity

data class DropArtifact(override val context: GameContext,
                        override val source: GameEntity<EntityType>,
                        val artifactId : Int): GameCommand<EntityType>
