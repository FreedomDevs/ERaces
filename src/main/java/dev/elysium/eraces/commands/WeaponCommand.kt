package dev.elysium.eraces.commands

import com.mojang.brigadier.tree.LiteralCommandNode
import dev.elysium.eraces.commands.executors.weapon.WeaponAllExec
import dev.elysium.eraces.commands.executors.weapon.WeaponGiveExec
import dev.elysium.eraces.commands.executors.weapon.WeaponInfoExec
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands

object WeaponCommand {
    val cmd: LiteralCommandNode<CommandSourceStack> =
        Commands.literal("weapon")
            .then(WeaponGiveExec.cmd)
            .then(WeaponAllExec.cmd)
            .then(WeaponInfoExec.cmd)
            .build()
}
