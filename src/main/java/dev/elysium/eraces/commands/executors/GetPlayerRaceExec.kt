package dev.elysium.eraces.commands.executors

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.elysium.eraces.ERaces.Companion.getInstance
import dev.elysium.eraces.utils.msg
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver

object GetPlayerRaceExec {
    val cmd: LiteralArgumentBuilder<CommandSourceStack> = Commands.literal("get_player_race")
        .then(
            Commands.argument("target", ArgumentTypes.player())
                .executes { ctx: CommandContext<CommandSourceStack> -> this.exec(ctx) }
        )

    @Suppress("SameReturnValue")
    private fun exec(ctx: CommandContext<CommandSourceStack>): Int {
        val source = ctx.source ?: return Command.SINGLE_SUCCESS
        val messages = getInstance().context.messageManager.data
        val players = ctx.getArgument("target", PlayerSelectorArgumentResolver::class.java).resolve(source)

        if (players.isEmpty()) {
            source.sender.msg(messages.playerNotFound)
            return Command.SINGLE_SUCCESS
        }
        if (players.size > 1) {
            source.sender.msg(messages.multiplePlayersSelected, Pair("{count}", players.size.toString()))
            return Command.SINGLE_SUCCESS
        }
        val player = players.first()
        val playerRace = getInstance().context.playerDataManager.getPlayerRaceId(player)

        if (source.sender.name == player.name) {
            source.sender.msg(messages.getPlayerRaceSuccessMe)
        } else {
            source.sender.msg(
                messages.getPlayerRaceSuccess,
                Pair("{player}", player.name), Pair("{race}", playerRace)
            )
        }

        return Command.SINGLE_SUCCESS
    }
}
