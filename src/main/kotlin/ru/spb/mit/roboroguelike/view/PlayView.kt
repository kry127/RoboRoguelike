package ru.spb.mit.roboroguelike.view

import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.GameComponents
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.extensions.onComponentEvent
import org.hexworks.zircon.api.extensions.onKeyboardEvent
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.mvc.base.BaseView
import org.hexworks.zircon.api.uievent.ComponentEventType
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Processed
import ru.spb.mit.roboroguelike.Game
import ru.spb.mit.roboroguelike.GameBlock
import ru.spb.mit.roboroguelike.GameBuilder
import ru.spb.mit.roboroguelike.objects.GameConfig

class PlayView(private val game: Game = GameBuilder.defaultGame()) : BaseView() {

    override val theme = ColorThemes.afterglow()

    override fun onDock() {

        val sidebar = Components.panel()
                .withSize(GameConfig.SIDEBAR_WIDTH, GameConfig.WINDOW_HEIGHT)
                .wrapWithBox()
                .build()

        sidebar.addFragment(PlayerStatusFragment(
                width = sidebar.contentSize.width,
                player = game.player))


        val saveGameButton = Components.button()
                .withAlignmentWithin(sidebar, ComponentAlignment.BOTTOM_CENTER)
                .withText("Save")
                .wrapSides(false)
                .withBoxType(BoxType.SINGLE)
                .wrapWithShadow()
                .wrapWithBox()
                .build()


        val mainMenuButton = Components.button()
                .withAlignmentAround(saveGameButton, ComponentAlignment.TOP_CENTER)
                .withText("Main menu")
                .wrapSides(false)
                .withBoxType(BoxType.SINGLE)
                .wrapWithShadow()
                .wrapWithBox()
                .build()

        sidebar.addComponent(saveGameButton)
        sidebar.addComponent(mainMenuButton)

        val logArea = Components.logArea()
                .withTitle("Log")
                .wrapWithBox()
                .withSize(GameConfig.WINDOW_WIDTH - GameConfig.SIDEBAR_WIDTH, GameConfig.LOG_AREA_HEIGHT)
//                .withSize(GameConfig.WINDOW_WIDTH, GameConfig.LOG_AREA_HEIGHT)
                .withAlignmentWithin(screen, ComponentAlignment.BOTTOM_RIGHT)
                .build()


        val gameComponent = GameComponents.newGameComponentBuilder<Tile, GameBlock>()
                .withGameArea(game.world)
                .withVisibleSize(game.world.visibleSize())
                .withProjectionMode(ProjectionMode.TOP_DOWN)
                .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
                .build()

        screen.addComponent(gameComponent)
        screen.addComponent(logArea)
        screen.addComponent(sidebar)

        saveGameButton.onComponentEvent(ComponentEventType.ACTIVATED) {
            game.defaultSerialize()
            Processed
        }

        mainMenuButton.onComponentEvent(ComponentEventType.ACTIVATED) {
            replaceWith(StartView())
            close()
            Processed
        }

        screen.onKeyboardEvent(KeyboardEventType.KEY_PRESSED) { event, _ ->
            game.world.update(screen, event, game)
            Processed
        }

    }
}