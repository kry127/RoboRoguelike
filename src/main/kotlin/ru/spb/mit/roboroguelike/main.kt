package ru.spb.mit.roboroguelike

import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Screens
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.component.ComponentAlignment
import ru.spb.mit.roboroguelike.map.BooleanWorldMap
import ru.spb.mit.roboroguelike.map.generator.SimpleRoomGenerator
import ru.spb.mit.roboroguelike.objects.GameConfig
import ru.spb.mit.roboroguelike.view.StartView

@Suppress("ConstantConditionIf")
fun main(args: Array<String>) {
    val application = SwingApplications.startApplication(GameConfig.buildAppConfig())
    application.dock(StartView())
}
