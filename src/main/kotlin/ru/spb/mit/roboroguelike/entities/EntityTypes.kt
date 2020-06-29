package ru.spb.mit.roboroguelike.entities

import org.hexworks.amethyst.api.base.BaseEntityType

object Player : BaseEntityType(
        name = "player")

object AggressiveMob : BaseEntityType(
        name = "aggressive-mob")

object CowardMob : BaseEntityType(
        name = "coward-mob")

object StaticMob : BaseEntityType(
        name = "static-mob")

object Wall : BaseEntityType(
        name = "wall")

object LadderUp: BaseEntityType (
        name = "LadderUp"
), Teleport

object LadderDown: BaseEntityType(
        name = "LadderDown"
), Teleport

object RegularHealthBox: BaseEntityType(
        name = "regular-health-box"
), HealthBox

object SuperHealthBox: BaseEntityType(
        name = "super-health-box"
), HealthBox

object StatsArtifact: BaseEntityType(
        name = "stats-artifact"
), Artifact

object HealthArtifact: BaseEntityType(
        name = "stats-artifact"
), Artifact