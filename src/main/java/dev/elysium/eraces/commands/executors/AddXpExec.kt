package dev.elysium.eraces.commands.executors

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.elysium.eraces.ERaces.Companion.getInstance
import dev.elysium.eraces.utils.msg
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver

object AddXpExec {
    val cmd: LiteralArgumentBuilder<CommandSourceStack> = Commands.literal("xp_add").then(
        Commands.argument("target", ArgumentTypes.player()).then(
            Commands.argument<Long>("count", LongArgumentType.longArg(1, Long.MAX_VALUE))
                .executes { ctx: CommandContext<CommandSourceStack> -> exec(ctx) })
    )

    @Suppress("SameReturnValue")
    private fun exec(ctx: CommandContext<CommandSourceStack>): Int {
        val source = ctx.source ?: return Command.SINGLE_SUCCESS
        val messages = getInstance().context.messageManager.data

        val players = ctx.getArgument("target", PlayerSelectorArgumentResolver::class.java).resolve(source)
        val count = ctx.getArgument("count", Long::class.javaPrimitiveType)

        if (players.isEmpty()) {
            source.sender.msg(messages.playerNotFound)
            return Command.SINGLE_SUCCESS
        }
        if (players.size > 1) {
            source.sender.msg(messages.multiplePlayersSelected, Pair("{count}", players.size.toString()))
            return Command.SINGLE_SUCCESS
        }
        val player = players.first()

        getInstance().context.specializationsManager.addXp(player, count)

        source.sender.msg(
            messages.addXpResponse, Pair("{player}", player.name), Pair("{count}", count.toString())
        )
        return Command.SINGLE_SUCCESS
    }
}
