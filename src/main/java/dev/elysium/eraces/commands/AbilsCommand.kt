package dev.elysium.eraces.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.tree.LiteralCommandNode
import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.AbilsManager
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.entity.Player

class AbilsCommand {

    val cmd: LiteralCommandNode<CommandSourceStack> = Commands.literal("abils")
        .requires { source -> source.sender is Player }
        .then(
            Commands.argument("id", StringArgumentType.word())
                .executes { context ->
                    val sender = context.source.sender
                    if (sender !is Player) {
                        sender.sendMessage("§cТолько игрок может использовать способности")
                        return@executes Command.SINGLE_SUCCESS
                    }

                    val id = StringArgumentType.getString(context, "id")
                    AbilsManager.getInstance().activate(sender, id)
                    Command.SINGLE_SUCCESS
                }
        )
        .executes { context ->
            val sender = context.source.sender
            sender.sendMessage("§eИспользование: /abils <id>")
            Command.SINGLE_SUCCESS
        }
        .build()
}
