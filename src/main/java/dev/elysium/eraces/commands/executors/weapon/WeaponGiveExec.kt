package dev.elysium.eraces.commands.executors.weapon

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import dev.elysium.eraces.commands.CommandSuggestions
import dev.elysium.eraces.items.core.ItemFactory
import dev.elysium.eraces.items.core.ItemRegistry
import dev.elysium.eraces.items.weapon.MeleeWeapon
import dev.elysium.eraces.utils.msg
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.Bukkit

object WeaponGiveExec {
    val cmd = Commands.literal("give")
        .then(
            Commands.argument("player", StringArgumentType.word())
                .suggests { _, builder ->
                    CommandSuggestions.onlinePlayers(builder).buildFuture()
                }
                .then(
                    Commands.argument("id", StringArgumentType.word())
                        .suggests { _, builder ->
                            CommandSuggestions.weaponIds(builder).buildFuture()
                        }
                        .executes { ctx ->
                            val sender = ctx.source.sender
                            val playerName = StringArgumentType.getString(ctx, "player")
                            val id = StringArgumentType.getString(ctx, "id")

                            val player = Bukkit.getPlayerExact(playerName)
                                ?: run {
                                    sender.msg("&cИгрок &e{player} &cне найден", "{player}" to playerName)
                                    return@executes 0
                                }

                            val weapon = ItemRegistry.byId(id) as? MeleeWeapon
                                ?: run {
                                    sender.msg("&cОружие с ID &e{id} &cне найдено", "{id}" to id)
                                    return@executes 0
                                }

                            player.inventory.addItem(ItemFactory.create(weapon))

                            player.msg(
                                "&aВы получили оружие &e{id}",
                                "{id}" to weapon.id
                            )

                            sender.msg(
                                "&aОружие &e{id} &aвыдано игроку &e{player}",
                                "{id}" to weapon.id,
                                "{player}" to player.name
                            )

                            Command.SINGLE_SUCCESS
                        }
                )
        )
}
