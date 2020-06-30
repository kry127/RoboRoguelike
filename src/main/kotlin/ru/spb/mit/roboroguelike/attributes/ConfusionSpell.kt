package ru.spb.mit.roboroguelike.attributes

import org.hexworks.cobalt.databinding.api.converter.Converter
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component

/**
 * This class is an attribute of player that represents confusion duration.
 * The confusion is casted by green coward mobs
 */
class ConfusionSpell(initialDuration: Int) : DisplayableAttribute {
    private val durationProperty = createPropertyFrom(initialDuration)
    var confusionDuration: Int by durationProperty.asDelegate()


    override fun toComponent(width: Int): Component {

        val confusionDescriptionLabel = Components.label()
                .withSize(width, 1)
                .build()

        confusionDescriptionLabel.textProperty.bind(durationProperty, object : Converter<Int, String> {
            override fun convert(source: Int): String {
                return if (source > 0) {
                    "Confusion: $source s."
                } else ""
            }

        })

        return confusionDescriptionLabel
    }
}