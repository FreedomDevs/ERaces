package dev.elysium.eraces.commands.executors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.RacesReloader;
import dev.elysium.eraces.VisualsManager;
import dev.elysium.eraces.utils.ChatUtil;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SetPlayerRaceExec {
    @Getter
    private final LiteralArgumentBuilder<CommandSourceStack> cmd;

    public SetPlayerRaceExec() {
        this.cmd = Commands.literal("set_player_race")
                .then(Commands.argument("target", ArgumentTypes.player())
                        .then(Commands.argument("raceId", StringArgumentType.word())
                                .suggests(this::suggest)
                                .executes(this::exec)));
    }

    private CompletableFuture<Suggestions> suggest(final CommandContext<CommandSourceStack> ctx, final SuggestionsBuilder builder) {
        ERaces.getInstance().getContext().getRacesConfigManager().getRaces()
                .keySet().stream()
                .filter(key -> key.toLowerCase().startsWith(builder.getRemainingLowerCase()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }

    @SuppressWarnings("SameReturnValue")
    private int exec(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Player player = ctx.getArgument("target", PlayerSelectorArgumentResolver.class)
                .resolve(ctx.getSource())
                .getFirst();
        String newRace = ctx.getArgument("raceId", String.class);

        var context = ERaces.getInstance().getContext();

        if (!context.getRacesConfigManager().getRaces().containsKey(newRace)) {
            String message = context.getMessageManager().getData().getSetPlayerRaceNotFound();
            ChatUtil.message(ctx.getSource().getSender(), message);
            return Command.SINGLE_SUCCESS;
        }

        context.getPlayerDataManager().setPlayerRace(player, newRace);
        RacesReloader.reloadRaceForPlayer(player);
        VisualsManager.updateVisualsForPlayer(player);

        String message = context.getMessageManager().getData().getSetPlayerRaceSuccess();
        ChatUtil.message(ctx.getSource().getSender(),
                message,
                Map.of("{player}", player.getName(), "{race}", newRace));

        return Command.SINGLE_SUCCESS;
    }
}
