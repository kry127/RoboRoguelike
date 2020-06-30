/**
 * This file contains a list of commands, that are beeing processed by game engine.
 * They are defining chain processing of commands (in order to execute one command
 * you need to execute bunch of other commands)
 * All names are self-descripting. Common parameters: common game context and
 * the source (cause) of the command invocation
 */

package ru.spb.mit.roboroguelike.commands

import org.hexworks.amethyst.api.entity.EntityType
import ru.spb.mit.roboroguelike.GameContext
import ru.spb.mit.roboroguelike.entities.GameCommand
import ru.spb.mit.roboroguelike.entities.GameEntity
import org.hexworks.zircon.api.data.impl.Position3D
import ru.spb.mit.roboroguelike.entities.HealthBox

data class AttemptTeleportation(override val context: GameContext,
                                override val source: GameEntity<EntityType>): GameCommand<EntityType>

data class Consume(override val context: GameContext,
                   override val source: GameEntity<HealthBox>,
                   val position: Position3D) : GameCommand<EntityType>



data class DropArtifact(override val context: GameContext,
                        override val source: GameEntity<EntityType>,
                        val artifactId : Int): GameCommand<EntityType>

data class MoveCamera(override val context: GameContext,
                      override val source: GameEntity<EntityType>,
                      val position: Position3D): GameCommand<EntityType>


data class MoveTo(override val context: GameContext,
                  override val source: GameEntity<EntityType>,
                  val position: Position3D) : GameCommand<EntityType>


data class Remove(override val context: GameContext,
                  override val source: GameEntity<EntityType>) : GameCommand<EntityType>

data class TakeArtifact(override val context: GameContext,
                        override val source: GameEntity<EntityType>): GameCommand<EntityType>