package ru.spb.mit.roboroguelike.attributes

import org.hexworks.amethyst.api.Attribute

/**
 * This is attributes that improves over Player's level
 */
interface ProgressiveAttribute : Attribute {

    /**
     * The function is called every time player has been leveled up
     */
    fun onLevelUp(newLvl: Int)
}
