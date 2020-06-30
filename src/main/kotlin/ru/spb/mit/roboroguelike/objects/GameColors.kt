package ru.spb.mit.roboroguelike.objects

import org.hexworks.zircon.api.TileColors

/**
 * Singleton object that specifies game colors (constants)
 */
object GameColors {
    val WALL_FOREGROUND = TileColors.fromString("#75715E")
    val WALL_BACKGROUND = TileColors.fromString("#3E3D32")

    val FLOOR_FOREGROUND = TileColors.fromString("#75715E")
    val FLOOR_BACKGROUND = TileColors.fromString("#1e2320")

    val HEALTH_BOX_BACKGROUND = TileColors.fromString("#DDCCEE")
    val HEALTH_BOX_FOREGROUND_LITE = TileColors.fromString("#005500")
    val HEALTH_BOX_FOREGROUND_MEDIUM = TileColors.fromString("#CC7700")
    val HEALTH_BOX_FOREGROUND_HEAVY = TileColors.fromString("#FF0022")
    val HEALTH_BOX_FOREGROUND_MEGA = TileColors.fromString("#0000FF")

    val ARTEFACT_FOREGROUND_COLOR = TileColors.fromString("#AA7700")

    val ACCENT_COLOR = TileColors.fromString("#FFCD22")
    val LADDER_COLOR = TileColors.fromString("#0fdb61")

    val RED = TileColors.fromString("#FF0000")
    val GREEN = TileColors.fromString("#71FF33")
    val YELLOW = TileColors.fromString("#fff70a")
}