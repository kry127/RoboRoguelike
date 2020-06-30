package ru.spb.mit.roboroguelike.attributes

import org.junit.*
import org.junit.Assert.*

internal class EntityExperienceTest {
    @Test
    fun checkXPToLevelConversion() {
        var gainedXp = 0
        var gainedXpDelta = 0
        for (level in 1..100) {
            assertEquals(EntityExperience.xpToLevel(gainedXp), level)
            assertEquals(EntityExperience.xpToLevel(gainedXp + 50), level)
            assertEquals(EntityExperience.xpToLevel(gainedXp + 100), level)
            gainedXpDelta += 200
            gainedXp += gainedXpDelta
        }
    }

    @Test
    fun checkLevelToXPConversion() {
        var gainedXp = 0
        var gainedXpDelta = 0
        for (level in 1..100) {
            assertEquals(EntityExperience.levelToXp(level), gainedXp)
            gainedXpDelta += 200
            gainedXp += gainedXpDelta
        }
    }
}