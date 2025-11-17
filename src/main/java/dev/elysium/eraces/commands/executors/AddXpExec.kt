package dev.elysium.eraces.commands.executors

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.elysium.eraces.ERaces.Companion.getInstance
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import org.bukkit.entity.Player

object AddXpExec {
    val cmd: LiteralArgumentBuilder<CommandSourceStack> = Commands.literal("xp_add")
        .then(
            Commands.argument("target", ArgumentTypes.player())
                .then(
                    Commands.argument<Long>("count", LongArgumentType.longArg(1, Long.MAX_VALUE))
                        .executes { ctx: CommandContext<CommandSourceStack> -> exec(ctx) }
                )
        )

    private fun exec(ctx: CommandContext<CommandSourceStack>): Int {
        val player: Player =
            ctx.getArgument("target", PlayerSelectorArgumentResolver::class.java)
                .resolve(ctx.getSource()!!).first()
        val count = ctx.getArgument("count", Long::class.javaPrimitiveType)

        getInstance().context.specializationsManager.addXp(player, count)
        return Command.SINGLE_SUCCESS
    }
}
