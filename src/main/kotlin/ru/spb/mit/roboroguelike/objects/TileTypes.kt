package ru.spb.mit.roboroguelike.objects

import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.graphics.Symbols
import org.hexworks.zircon.api.resource.TilesetResource

enum class TileTypes(val tile: CharacterTile) {
    EMPTY(Tiles.empty()),
    FLOOR(Tiles.newBuilder()
            .withCharacter(Symbols.INTERPUNCT)
            .withForegroundColor(GameColors.FLOOR_FOREGROUND)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .buildCharacterTile()),

    WALL(Tiles.newBuilder()
            .withCharacter(Symbols.BLOCK_DENSE)
            .withForegroundColor(GameColors.WALL_FOREGROUND)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .buildCharacterTile()),

    PLAYER(Tiles.newBuilder()
            .withCharacter('@')
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.ACCENT_COLOR)
            .buildCharacterTile()),

    AGGRESSIVE_MOB(Tiles.newBuilder()
            .withCharacter(Symbols.FACE_WHITE)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.RED)
            .buildCharacterTile()),

    COWARDLY_MOB(Tiles.newBuilder()
            .withCharacter(Symbols.FACE_BLACK)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.GREEN)
            .buildCharacterTile()),

    STATIC_MOB(Tiles.newBuilder()
            .withTileset(CP437TilesetResources.bisasam16x16())
            .withCharacter(Symbols.OMEGA)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.YELLOW)
            .buildCharacterTile()),

    LADDER_UP (Tiles.newBuilder()
            .withCharacter(Symbols.TRIANGLE_UP_POINTING_BLACK)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.LADDER_COLOR)
            .buildCharacterTile()),

    LADDER_DOWN (Tiles.newBuilder()
            .withCharacter(Symbols.TRIANGLE_DOWN_POINTING_BLACK)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.LADDER_COLOR)
            .buildCharacterTile()),

    // health boxes
    HEALTH_BOX_LITE(Tiles.newBuilder()
        .withCharacter(Symbols.DOUBLE_LINE_CROSS)
        .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
        .withForegroundColor(GameColors.HEALTH_BOX_FOREGROUND_LITE)
        .buildCharacterTile()),
    HEALTH_BOX_MEDIUM(Tiles.newBuilder()
            .withCharacter(Symbols.DOUBLE_LINE_CROSS)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.HEALTH_BOX_FOREGROUND_MEDIUM)
            .buildCharacterTile()),
    HEALTH_BOX_HEAVY(Tiles.newBuilder()
            .withCharacter(Symbols.DOUBLE_LINE_CROSS)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.HEALTH_BOX_FOREGROUND_HEAVY)
            .buildCharacterTile()),
    HEALTH_BOX_MEGA(Tiles.newBuilder()
            .withCharacter(Symbols.HEART)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.HEALTH_BOX_FOREGROUND_MEGA)
            .buildCharacterTile()),
    // end health boxes
}