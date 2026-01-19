package dev.elysium.eraces.commands.executors.weapon

import com.mojang.brigadier.arguments.StringArgumentType
import dev.elysium.eraces.commands.CommandSuggestions
import dev.elysium.eraces.items.core.ItemRegistry
import dev.elysium.eraces.items.weapon.MeleeWeapon
import dev.elysium.eraces.utils.msg
import io.papermc.paper.command.brigadier.Commands

object WeaponInfoExec {
    val cmd = Commands.literal("info")
        .then(
            Commands.argument("id", StringArgumentType.word())
                .suggests { _, builder ->
                    CommandSuggestions.weaponIds(builder).buildFuture()
                }
                .executes { ctx ->
                    val sender = ctx.source.sender
                    val id = StringArgumentType.getString(ctx, "id")

                    val weapon = ItemRegistry.byId(id) as? MeleeWeapon
                        ?: run {
                            sender.msg("&cОружие с ID &e{id} &cне найдено", "{id}" to id)
                            return@executes 0
                        }

                    sender.msg(
                        "&6Оружие: &e{id}",
                        "{id}" to weapon.id
                    )

                    sender.msg(
                        " &7Материал: &f{material}",
                        "{material}" to weapon.material.name
                    )

                    sender.msg(
                        " &7Урон: &f{damage}",
                        "{damage}" to weapon.damage.toString()
                    )

                    sender.msg(
                        " &7Скорость атаки: &f{speed}",
                        "{speed}" to weapon.attackSpeed.toString()
                    )

                    sender.msg(
                        " &7Прочность: &f{durability}",
                        "{durability}" to weapon.maxDurability.toString()
                    )

                    1
                }
        )
}
