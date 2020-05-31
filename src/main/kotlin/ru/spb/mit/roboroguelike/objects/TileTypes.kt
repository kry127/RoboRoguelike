package ru.spb.mit.roboroguelike.objects

import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.graphics.Symbols

object TileTypes {
    val EMPTY: CharacterTile = Tiles.empty()

    val FLOOR: CharacterTile = Tiles.newBuilder()
            .withCharacter(Symbols.INTERPUNCT) // 1
            .withForegroundColor(GameColors.FLOOR_FOREGROUND) // 2
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND) // 3
            .buildCharacterTile() // 4

    val WALL: CharacterTile = Tiles.newBuilder()
            .withCharacter('#')
            .withForegroundColor(GameColors.WALL_FOREGROUND)
            .withBackgroundColor(GameColors.WALL_BACKGROUND)
            .buildCharacterTile()

    val PLAYER = Tiles.newBuilder()
            .withCharacter('@')
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.ACCENT_COLOR)
            .buildCharacterTile()

    val AGGRESSIVE_MOB = Tiles.newBuilder()
            .withCharacter('x')
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.RED)
            .buildCharacterTile()

    val COWARDLY_MOB = Tiles.newBuilder()
            .withCharacter('x')
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.GREEN)
            .buildCharacterTile()

    val STATIC_MOB = Tiles.newBuilder()
            .withCharacter('x')
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.YELLOW)
            .buildCharacterTile()

    val LADDER_UP = Tiles.newBuilder()
            .withCharacter('U')
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.LADDER_COLOR)
            .buildGraphicTile()

    val LADDER_DOWN = Tiles.newBuilder()
            .withCharacter('D')
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.LADDER_COLOR)
            .buildGraphicTile()
}