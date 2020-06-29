package ru.spb.mit.roboroguelike.objects

import org.hexworks.zircon.api.AppConfigs
import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Sizes

object GameConfig {
    const val DUNGEON_LEVELS = 5 // 1

    // look & feel
    val TILESET = CP437TilesetResources.guybrush16x16() // 2
    val THEME = ColorThemes.zenburnVanilla() // 3
    const val SIDEBAR_WIDTH = 18
    const val LOG_AREA_HEIGHT = 8 // 4

    // sizing
    const val WINDOW_WIDTH = 80
    const val WINDOW_HEIGHT = 50

    // tracking box
    const val HORIZONTAL_LUFT = 30
    const val VERTICAL_LUFT = 20

    // save file
    const val SAVE_FILE_PATH = "RevoRoguelike.dat"

    // DO NOT CHANGE THE NUMBER OF ARTIFACT SLOTS (needs refactor)
    const val NUMBER_OF_ARTIFACT_SLOTS = 4
    const val STATIC_DROP_CHANCE = 0.07
    const val COWARD_DROP_CHANCE = 0.85
    const val AGGRESSIVE_DROP_CHANCE = 0.0

//    val WORLD_SIZE = Sizes.create3DSize(200, 200, DUNGEON_LEVELS)
    val WORLD_SIZE = Sizes.create3DSize(600, 800, DUNGEON_LEVELS)
    //val VISIBLE_SIZE = Sizes.create3DSize(600, 800, DUNGEON_LEVELS)
    val VISIBLE_SIZE = Sizes.create3DSize(
            xLength = GameConfig.WINDOW_WIDTH - GameConfig.SIDEBAR_WIDTH,
            yLength = GameConfig.WINDOW_HEIGHT - GameConfig.LOG_AREA_HEIGHT,
            zLength = 1)

    fun buildAppConfig() = AppConfigs.newConfig() // 5
            .enableBetaFeatures()
            .withTitle("RevoRoguelike [free license]")
            .withDefaultTileset(TILESET)
            .withSize(Sizes.create(WINDOW_WIDTH, WINDOW_HEIGHT))
            .build()
}