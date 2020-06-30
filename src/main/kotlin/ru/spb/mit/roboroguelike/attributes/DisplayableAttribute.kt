package ru.spb.mit.roboroguelike.attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.zircon.api.component.Component

/**
 * This is a general form of attribute that can be displayed in the side panel.
 * Extend attribute from this interface and use attribute in the player entity
 * to display it on the sidebar
 */
interface DisplayableAttribute : Attribute {

    /**
     * This method defines a way to render attribute contents in the sidebar.
     * Override it to display contents
     */
    fun toComponent(width: Int): Component

}
