package dev.elysium.eraces.commands.executors

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.elysium.eraces.ERaces.Companion.getInstance
import dev.elysium.eraces.RacesReloader
import dev.elysium.eraces.VisualsManager
import dev.elysium.eraces.utils.msg
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import java.util.*
import java.util.concurrent.CompletableFuture

object SetPlayerRaceExec {
    val cmd: LiteralArgumentBuilder<CommandSourceStack> =
        Commands.literal("set_player_race").then(
            Commands.argument("target", ArgumentTypes.player()).then(
                Commands.argument<String>("raceId", StringArgumentType.word())
                    .suggests { ctx: CommandContext<CommandSourceStack>, builder: SuggestionsBuilder ->
                        this.suggest(ctx, builder)
                    }.executes { ctx: CommandContext<CommandSourceStack> -> this.exec(ctx) })
        )

    @Suppress("unused")
    private fun suggest(
        ctx: CommandContext<CommandSourceStack>, builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        getInstance().context.racesConfigManager.races.keys.stream()
            .filter { key: String -> key.lowercase(Locale.getDefault()).startsWith(builder.remainingLowerCase) }
            .forEach { text: String -> builder.suggest(text) }
        return builder.buildFuture()
    }

    @Suppress("SameReturnValue")
    private fun exec(ctx: CommandContext<CommandSourceStack>): Int {
        val source = ctx.source ?: return Command.SINGLE_SUCCESS
        val context = getInstance().context
        val messages = context.messageManager.data

        val players = ctx.getArgument("target", PlayerSelectorArgumentResolver::class.java).resolve(source)
        val newRace = ctx.getArgument("raceId", String::class.java)

        if (players.isEmpty()) {
            source.sender.msg(messages.playerNotFound)
            return Command.SINGLE_SUCCESS
        }
        if (players.size > 1) {
            source.sender.msg(messages.multiplePlayersSelected, Pair("{count}", players.size.toString()))
            return Command.SINGLE_SUCCESS
        }
        val player = players.first()

        if (!context.racesConfigManager.races.containsKey(newRace)) {
            source.sender.msg(messages.setPlayerRaceNotFound)
            return Command.SINGLE_SUCCESS
        }

        context.playerDataManager.setPlayerRace(player, newRace)
        RacesReloader.reloadRaceForPlayer(player)
        VisualsManager.updateVisualsForPlayer(player)

        source.sender.msg(
            messages.setPlayerRaceSuccess,
            Pair("{player}", player.name), Pair("{race}", newRace)
        )
        return Command.SINGLE_SUCCESS
    }
}
