package ru.spb.mit.roboroguelike.view

import org.hexworks.cavesofzircon.attributes.DisplayableAttribute
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.ListItem
import ru.spb.mit.roboroguelike.entities.GameEntity
import ru.spb.mit.roboroguelike.entities.Player

class PlayerStatusFragment(
        width: Int,
        player: GameEntity<Player>) : Fragment {

    override val root = Components.panel()
            .withSize(width, 30)
            .build().apply {
                val header = Components.textBox()
                        .withContentWidth(width)
                        .withAlignmentWithin(this, ComponentAlignment.TOP_CENTER)
                        .addHeader(" == Status ==")
                        .build()
                addComponent(header)

                // initiate rendering from offset position and further
                var offset = 2
                player.attributes.filterIsInstance<DisplayableAttribute>()
                        .forEach {
                            val component = it.toComponent(width)
                            component.moveDownBy(offset)
                            offset += component.height
                            addComponent(component)
                        }
            }
}
