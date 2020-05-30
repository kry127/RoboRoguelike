package ru.spb.mit.roboroguelike

import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Screens
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.component.ComponentAlignment
import ru.spb.mit.roboroguelike.map.BooleanWorldMap
import ru.spb.mit.roboroguelike.map.generator.SimpleRoomGenerator

@Suppress("ConstantConditionIf")
fun main(args: Array<String>) {

    val grid = SwingApplications.startTileGrid()
    val screen = Screens.createScreenFor(grid)

    val builder = SimpleRoomGenerator.Builder()
    val roomGenerator = builder.height(50).width(50).room_min_size(7).build()
    val map = roomGenerator.nextMap { i: Int, j: Int ->
        // coords of walls
        println("Wall at: ($i, $j)")
    }
    map.print()

    screen.addComponent(Components.header()
            .withText("Hello, from RoboRoguelike!")
            .withAlignmentWithin(screen, ComponentAlignment.CENTER))

    screen.applyColorTheme(ColorThemes.arc())
    screen.display()

}
