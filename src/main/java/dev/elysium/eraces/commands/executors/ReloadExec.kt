package dev.elysium.eraces.commands.executors

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.elysium.eraces.ERaces.Companion.getInstance
import dev.elysium.eraces.RacesReloader
import dev.elysium.eraces.VisualsManager
import dev.elysium.eraces.utils.msg
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands

object ReloadExec {
    val cmd: LiteralArgumentBuilder<CommandSourceStack> = Commands.literal("reload")
        .executes({ ctx: CommandContext<CommandSourceStack> -> this.exec(ctx) })

    @Suppress("SameReturnValue")
    private fun exec(ctx: CommandContext<CommandSourceStack>): Int {
        val source = ctx.source ?: return Command.SINGLE_SUCCESS
        val context = getInstance().context

        context.racesConfigManager.reloadConfig()
        context.specializationsManager.reloadConfig()
        RacesReloader.reloadRaceForAllPlayers()
        VisualsManager.reloadVisualsForAllPlayer()

        source.sender.msg(context.messageManager.data.reloadSuccess)

        return Command.SINGLE_SUCCESS
    }
}
