package ru.spb.mit.roboroguelike.view

import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.GameComponents
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.mvc.base.BaseView
import ru.spb.mit.roboroguelike.Game
import ru.spb.mit.roboroguelike.GameBlock
import ru.spb.mit.roboroguelike.objects.GameConfig

class PlayView(private val game: Game = Game.create()) : BaseView() {

    override val theme = ColorThemes.arc()

    override fun onDock() {

        val sidebar = Components.panel()
                .withSize(GameConfig.SIDEBAR_WIDTH, GameConfig.WINDOW_HEIGHT)
                .wrapWithBox()
                .build()

        val logArea = Components.logArea()
                .withTitle("Log") // 1
                .wrapWithBox()  // 2
                .withSize(GameConfig.WINDOW_WIDTH - GameConfig.SIDEBAR_WIDTH, GameConfig.LOG_AREA_HEIGHT)
                .withAlignmentWithin(screen, ComponentAlignment.BOTTOM_RIGHT)  // 3
                .build()


        val gameComponent = GameComponents.newGameComponentBuilder<Tile, GameBlock>()
                .withGameArea(game.world)
                .withVisibleSize(game.world.visibleSize())
                .withProjectionMode(ProjectionMode.TOP_DOWN)
                .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
                .build()

//        screen.addComponent(sidebar)
        screen.addComponent(gameComponent)
//        screen.addComponent(logArea)
    }
}