package ru.spb.mit.roboroguelike.view

import ru.spb.mit.roboroguelike.attributes.DisplayableAttribute
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.Fragment
import ru.spb.mit.roboroguelike.Game
import ru.spb.mit.roboroguelike.attributes.EntityPosition
import ru.spb.mit.roboroguelike.entities.LadderDown
import ru.spb.mit.roboroguelike.entities.tryToFindAttribute

class PlayerStatusFragment(
        width: Int,
        game : Game) : Fragment {

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
                game.player.attributes.filterIsInstance<DisplayableAttribute>()
                        .forEach {
                            val component = it.toComponent(width)
                            component.moveDownBy(offset)
                            offset += component.height
                            addComponent(component)
                        }

                // find cell with teleport and display it's position
                val cellWithTeleport = game.world.fetchBlocksAtLevel(game.world.currentLevel)
                        .firstOrNull { cell -> cell.component2().entities.find {
                            entity->entity.type.equals(LadderDown)
                        } != null }
                if (cellWithTeleport != null) {
                    val teleportEntity = cellWithTeleport.component2().entities.find {
                        entity -> entity.type.equals(LadderDown)
                    }
                    if (teleportEntity != null) {
                        val attribute2 = teleportEntity.tryToFindAttribute(EntityPosition::class)
                        val component2 = attribute2.toComponent(width)
                        component2.moveDownBy(offset)
                        offset += component2.height
                        addComponent(component2)
                    }
                }

            }
}
