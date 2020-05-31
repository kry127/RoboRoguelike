package ru.spb.mit.roboroguelike.entities

import org.hexworks.amethyst.api.base.BaseEntityType

object Player : BaseEntityType(
        name = "player")

object Wall : BaseEntityType(
        name = "wall")

object LadderUp: BaseEntityType (
        name = "LadderUp"
), Teleport

object LadderDown: BaseEntityType(
        name = "LadderDown"
), Teleport