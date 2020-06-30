package ru.spb.mit.roboroguelike

import org.hexworks.zircon.api.SwingApplications
import ru.spb.mit.roboroguelike.objects.GameConfig
import ru.spb.mit.roboroguelike.view.StartView

@Suppress("ConstantConditionIf")
/**
 * Entry point of the program
 */
fun main() {
    val application = SwingApplications.startApplication(GameConfig.buildAppConfig())
    application.dock(StartView())
}
