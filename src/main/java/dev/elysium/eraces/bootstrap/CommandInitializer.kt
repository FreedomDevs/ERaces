package dev.elysium.eraces.bootstrap

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.commands.AbilsCommand
import dev.elysium.eraces.commands.MyRaceCommand
import dev.elysium.eraces.commands.RacesCommand
import dev.elysium.eraces.exceptions.internal.InitFailedException
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents

class CommandInitializer : IInitializer {
    override fun setup(plugin: ERaces) {
        try {
            plugin.lifecycleManager.registerEventHandler(
                LifecycleEvents.COMMANDS,
                LifecycleEventHandler { commands: ReloadableRegistrarEvent<Commands> ->
                    listOf(MyRaceCommand.cmd, RacesCommand.cmd, AbilsCommand.cmd)
                        .forEach { commands.registrar().register(it) }
                }
            )
        } catch (e: Exception) {
            throw InitFailedException("Ошибка при регистрации команд", e)
        }
    }
}
