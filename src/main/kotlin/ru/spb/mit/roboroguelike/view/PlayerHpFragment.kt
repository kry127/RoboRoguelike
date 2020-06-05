package ru.spb.mit.roboroguelike.view

import org.hexworks.cavesofzircon.attributes.DisplayableAttribute
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import ru.spb.mit.roboroguelike.entities.EntityHitpoints
import ru.spb.mit.roboroguelike.entities.GameEntity
import ru.spb.mit.roboroguelike.entities.Player

class PlayerStatsFragment(
        width: Int,
        player: GameEntity<Player>) : Fragment {

    override val root = Components.panel()
            .withSize(width, 30)
            .build().apply {
                addComponent(Components.header().withText("Player"))
                player.attributes.toList().filterIsInstance<DisplayableAttribute>()
                        .forEach {
                            addComponent(it.toComponent(width))
                        }
            }
}
