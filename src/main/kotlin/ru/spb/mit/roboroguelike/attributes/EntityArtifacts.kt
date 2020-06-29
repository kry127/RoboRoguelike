package ru.spb.mit.roboroguelike.attributes

import org.hexworks.cobalt.databinding.api.Properties
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.event.ChangeEvent
import org.hexworks.cobalt.databinding.api.event.ChangeListener
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import ru.spb.mit.roboroguelike.Game
import ru.spb.mit.roboroguelike.entities.*
import java.lang.Math.floor
import java.lang.Math.sqrt

class EntityArtifacts() : DisplayableAttribute {
    var slot1 : Maybe<GameEntity<Artifact>> = Maybe.empty()
    var slot2 : Maybe<GameEntity<Artifact>> = Maybe.empty()
    var slot3 : Maybe<GameEntity<Artifact>> = Maybe.empty()
    var slot4 : Maybe<GameEntity<Artifact>> = Maybe.empty()

    var artifactCountProp = Properties.propertyFrom(0)
    var artifactCount : Int by artifactCountProp.asDelegate()

    private fun artifactDescription(container : Maybe<GameEntity<Artifact>>) : String {
        if (!container.isPresent) {
            return "empty"
        }
        var result = ""
        val art = container.get()
        if (art.findAttribute(EntityPrimaryStats::class).isPresent) {
            result += " A=" + art.attack + " D=" + art.defence
        }
        if (art.findAttribute(EntityHitpoints::class).isPresent) {
            result += " HP=" + art.hp
        }
        return result
    }

    override fun toComponent(width: Int) : Component =Components.panel()
            .withSize(width, 6)
            .build().apply {

                val title = Components.label()
                        .withSize(width, 1)
                        .withPosition(0, 0)
                        .withText("Artifacts:")
                        .build()

                val slot1label = Components.label()
                        .withSize(width, 1)
                        .withPosition(0, 0)
                        .build()

                val slot2label = Components.label()
                        .withSize(width, 1)
                        .withPosition(0, 1)
                        .build()

                val slot3label = Components.label()
                        .withSize(width, 1)
                        .withPosition(0, 2)
                        .build()

                val slot4label = Components.label()
                        .withSize(width, 1)
                        .withPosition(0, 3)
                        .build()

                fun update() {
                    slot1label.text = "#1:" + artifactDescription(slot1);
                    slot2label.text = "#2:" + artifactDescription(slot2);
                    slot3label.text = "#3:" + artifactDescription(slot3);
                    slot4label.text = "#4:" + artifactDescription(slot4);
                }

                val changeEventListener : ChangeListener<Int> = object : ChangeListener<Int> {
                    override fun onChange(changeEvent: ChangeEvent<Int>) {
                        update()
                    }
                }

                artifactCountProp.onChange(changeEventListener)
                update()

                addComponent(slot1label)
                addComponent(slot2label)
                addComponent(slot3label)
                addComponent(slot4label)
            }
}