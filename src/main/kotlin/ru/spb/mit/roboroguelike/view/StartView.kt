package ru.spb.mit.roboroguelike.view

import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.extensions.onComponentEvent
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.mvc.base.BaseView
import org.hexworks.zircon.api.uievent.ComponentEventType
import org.hexworks.zircon.api.uievent.Processed
import ru.spb.mit.roboroguelike.GameBuilder
import ru.spb.mit.roboroguelike.WorldBuilder

class StartView : BaseView() {

    override val theme = ColorThemes.afterglow()

    override fun onDock() {
        val msg = "RevoRoguelike"
        val header = Components.textBox()
                .withContentWidth(msg.length)
                .addHeader(msg)
                .addNewLine()
                .withAlignmentWithin(screen, ComponentAlignment.CENTER)
                .build()
        val startButton = Components.button()
                .withAlignmentAround(header, ComponentAlignment.BOTTOM_CENTER)
                .withText("New game")
                .wrapSides(false)
                .withBoxType(BoxType.SINGLE)
                .wrapWithShadow()
                .wrapWithBox()
                .build()

        startButton.onComponentEvent(ComponentEventType.ACTIVATED) {
            replaceWith(PlayView())
            close()
            Processed
        }


        val loadGameButton = Components.button()
                .withAlignmentAround(startButton, ComponentAlignment.BOTTOM_CENTER)
                .withText("Load")
                .wrapSides(false)
                .withBoxType(BoxType.SINGLE)
                .wrapWithShadow()
                .wrapWithBox()
                .build()


        loadGameButton.onComponentEvent(ComponentEventType.ACTIVATED) {
            // reload same view, but with new world
            replaceWith(PlayView(GameBuilder.loadGame()))
            close()
            Processed
        }

        screen.addComponent(header)
        screen.addComponent(startButton)
        screen.addComponent(loadGameButton)
    }
}