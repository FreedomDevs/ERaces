package dev.elysium.eraces.commands.executors

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.elysium.eraces.items.RaceChangePotion
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.entity.Player

object GetChangePotionExec {
    val cmd: LiteralArgumentBuilder<CommandSourceStack> =
        Commands.literal("get_change_potion").requires { source: CommandSourceStack -> source.sender is Player }
            .executes { ctx: CommandContext<CommandSourceStack> ->
                (ctx.getSource()!!.sender as Player).inventory.addItem(RaceChangePotion.createCustomPotion())
                Command.SINGLE_SUCCESS
            }
}