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
        ERaces.getRacesMng().getRaces()
                .keySet().stream()
                .filter(key -> key.toLowerCase().startsWith(builder.getRemainingLowerCase()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }

    private int exec(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Player player = ctx.getArgument("target", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();
        String newRace = ctx.getArgument("raceId", String.class);

        if (!ERaces.getRacesMng().getRaces().containsKey(newRace)) {
            String message = ERaces.getMsgMng().getString("commands.set_player_race.race_not_found");
            ChatUtil.message(ctx.getSource().getSender(), message);
            return Command.SINGLE_SUCCESS;
        }

        ERaces.getPlayerMng().setPlayerRace(player, newRace);
        RacesReloader.reloadRaceForPlayer(player);
        VisualsManager.updateVisualsForPlayer(player);
        String message = ERaces.getMsgMng().getString("commands.set_player_race.set_success");
        ChatUtil.message(ctx.getSource().getSender(), message, Map.of("{player}", player.getName(), "{race}", newRace));
        return Command.SINGLE_SUCCESS;
    }
}
