package ru.spb.mit.roboroguelike.entities

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property

class EntityHitpoints(val maxHp: Int, hpInitial : Int) : Attribute {
    private val hpProperty = createPropertyFrom(hpInitial)
    var hp : Int by hpProperty.asDelegate()
}