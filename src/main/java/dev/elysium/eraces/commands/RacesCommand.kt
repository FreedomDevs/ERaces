package dev.elysium.eraces.commands

import com.mojang.brigadier.tree.LiteralCommandNode
import dev.elysium.eraces.commands.executors.*
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands

object RacesCommand {
    val cmd: LiteralCommandNode<CommandSourceStack> = Commands.literal("races")
        .requires { sender: CommandSourceStack ->
            sender.sender.hasPermission("eraces.races_command.use")
        }
        .then(ReloadExec.cmd)
        .then(GetPlayerRaceExec.cmd)
        .then(SetPlayerRaceExec.cmd)
        .then(GetChangePotionExec.cmd)
        .then(AddXpExec.cmd)
        .then(GetXpExec.cmd)
        .then(WeaponCommand.cmd)
        .build()
}
