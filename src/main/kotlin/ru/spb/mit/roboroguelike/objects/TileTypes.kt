package ru.spb.mit.roboroguelike.objects

import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.graphics.Symbols
import org.hexworks.zircon.api.resource.TilesetResource

object TileTypes {
    val EMPTY: CharacterTile = Tiles.empty()

    val FLOOR: CharacterTile = Tiles.newBuilder()
            .withCharacter(Symbols.INTERPUNCT) // 1
            .withForegroundColor(GameColors.FLOOR_FOREGROUND) // 2
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND) // 3
            .buildCharacterTile() // 4

    val WALL: CharacterTile = Tiles.newBuilder()
            .withCharacter(Symbols.BLOCK_DENSE)
            .withForegroundColor(GameColors.WALL_FOREGROUND)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .buildCharacterTile()

    val PLAYER = Tiles.newBuilder()
            .withCharacter('@')
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.ACCENT_COLOR)
            .buildCharacterTile()

    val AGGRESSIVE_MOB = Tiles.newBuilder()
            .withCharacter(Symbols.FACE_WHITE)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.RED)
            .buildCharacterTile()

    val COWARDLY_MOB = Tiles.newBuilder()
            .withCharacter(Symbols.FACE_BLACK)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.GREEN)
            .buildCharacterTile()

    val STATIC_MOB = Tiles.newBuilder()
            .withTileset(CP437TilesetResources.bisasam16x16())
            .withCharacter(Symbols.OMEGA)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.YELLOW)
            .buildCharacterTile()

    val LADDER_UP = Tiles.newBuilder()
            .withCharacter(Symbols.ARROW_UP)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.LADDER_COLOR)
            .buildCharacterTile()

    val LADDER_DOWN = Tiles.newBuilder()
            .withCharacter(Symbols.ARROW_DOWN)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.LADDER_COLOR)
            .buildCharacterTile()
}