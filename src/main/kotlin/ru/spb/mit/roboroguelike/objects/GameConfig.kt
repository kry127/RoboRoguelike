package ru.spb.mit.roboroguelike.objects

import org.hexworks.zircon.api.AppConfigs
import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Sizes

object GameConfig {
    const val DUNGEON_LEVELS = 2 // 1

    // look & feel
    val TILESET = CP437TilesetResources.rogueYun16x16() // 2
    val THEME = ColorThemes.zenburnVanilla() // 3
    const val SIDEBAR_WIDTH = 18
    const val LOG_AREA_HEIGHT = 8 // 4

    // sizing
    const val WINDOW_WIDTH = 80
    const val WINDOW_HEIGHT = 50

    // save file
    const val SAVE_FILE_PATH = "RoboRoguelike.dat"

    val WORLD_SIZE = Sizes.create3DSize(600, 800, DUNGEON_LEVELS)
//    val WORLD_SIZE = Sizes.create3DSize(WINDOW_WIDTH, WINDOW_HEIGHT, DUNGEON_LEVELS)
    //val VISIBLE_SIZE = Sizes.create3DSize(600, 800, DUNGEON_LEVELS)
    val VISIBLE_SIZE = Sizes.create3DSize(WINDOW_WIDTH, WINDOW_HEIGHT, DUNGEON_LEVELS)

    fun buildAppConfig() = AppConfigs.newConfig() // 5
            .enableBetaFeatures()
            .withDefaultTileset(TILESET)
            .withSize(Sizes.create(WINDOW_WIDTH, WINDOW_HEIGHT))
            .build()
}