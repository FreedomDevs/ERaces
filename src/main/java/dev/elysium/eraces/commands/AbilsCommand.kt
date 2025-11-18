package dev.elysium.eraces.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.AbilsManager
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

object AbilsCommand {
    val cmd: LiteralCommandNode<CommandSourceStack> = Commands.literal("abils")
        .requires { source -> source.sender is Player }
        .then(
            Commands.argument("id", StringArgumentType.word())
                .suggests { context, builder ->
                    suggestAbilities(context.source, builder)
                }
                .executes { context ->
                    val sender = context.source.sender
                    if (sender !is Player) {
                        sender.sendMessage("§cТолько игрок может использовать способности.")
                        return@executes Command.SINGLE_SUCCESS
                    }

                    val id = StringArgumentType.getString(context, "id")
                    val ability = AbilsManager.getInstance().getAbility(id)

                    if (ability == null) {
                        sender.sendMessage("§cСпособность §e$id §cне найдена.")
                        return@executes Command.SINGLE_SUCCESS
                    }

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

    private fun suggestAbilities(
        source: CommandSourceStack,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val sender = source.sender
        if (sender !is Player) return builder.buildFuture()

        val abilsManager = AbilsManager.getInstance()
        val race = ERaces.getInstance().context.playerDataManager.getPlayerRace(sender)

        val availableIds = race?.abilities ?: abilsManager.getAllAbilities().map { it.id }

        for (id in availableIds) {
            if (id.startsWith(builder.remaining, ignoreCase = true)) {
                builder.suggest(id)
            }
        }

        return builder.buildFuture()
    }
}

