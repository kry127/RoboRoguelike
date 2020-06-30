/**
 * This file describes key entity types in the game world.
 * The 'name' attribute should be unique, it is identifier of the type
 * of the entity during serialization/deserialization procedure.
 */

package ru.spb.mit.roboroguelike.entities

import org.hexworks.amethyst.api.base.BaseEntityType
import org.hexworks.amethyst.api.entity.EntityType


interface Artifact : EntityType
interface HealthBox : EntityType
interface Teleport : EntityType

object Player : BaseEntityType(
        name = "player")

object AggressiveMob : BaseEntityType(
        name = "aggressive-mob")

object CowardMob : BaseEntityType(
        name = "coward-mob")

object StaticMob : BaseEntityType(
        name = "static-mob")

object LadderUp : BaseEntityType(
        name = "LadderUp"
), Teleport

object LadderDown : BaseEntityType(
        name = "LadderDown"
), Teleport

object RegularHealthBox : BaseEntityType(
        name = "regular-health-box"
), HealthBox

object SuperHealthBox : BaseEntityType(
        name = "super-health-box"
), HealthBox

object StatsArtifact : BaseEntityType(
        name = "stats-artifact"
), Artifact

object HealthArtifact : BaseEntityType(
        name = "health-artifact"
), Artifact