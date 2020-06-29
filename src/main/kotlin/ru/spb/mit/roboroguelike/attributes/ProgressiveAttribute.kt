package ru.spb.mit.roboroguelike.attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.zircon.api.component.Component

/**
 * This is attributes that improves over Player's level
 */
interface ProgressiveAttribute : Attribute {
    fun onLevelUp(newLvl : Int)
}
