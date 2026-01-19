package dev.elysium.eraces.commands.executors.weapon

import com.mojang.brigadier.Command
import dev.elysium.eraces.items.core.ItemRegistry
import dev.elysium.eraces.items.core.ItemType
import dev.elysium.eraces.utils.msg
import io.papermc.paper.command.brigadier.Commands

object WeaponAllExec {
    val cmd = Commands.literal("all")
        .executes { ctx ->
            val sender = ctx.source.sender

            val weapons = ItemRegistry
                .all()
                .filter { it.type == ItemType.WEAPON }

            sender.msg(
                "&aЗарегистрировано оружий: &e{count}",
                "{count}" to weapons.size.toString()
            )

            weapons.forEach { weapon ->
                sender.msg(
                    " &7- &f{id}",
                    "{id}" to weapon.id
                )
            }

            Command.SINGLE_SUCCESS
        }
}
