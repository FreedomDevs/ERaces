package dev.elysium.eraces.commands.executors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.RacesReloader;
import dev.elysium.eraces.VisualsManager;
import dev.elysium.eraces.utils.ChatUtil;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class RegeneratePlayerRaceExec {
    @Getter
    private final LiteralArgumentBuilder<CommandSourceStack> cmd;

    public RegeneratePlayerRaceExec() {
        this.cmd = Commands.literal("regenerate_player_race")
                .then(Commands.argument("target", ArgumentTypes.player()).executes(this::exec));
    }

    private int exec(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSender sender = ctx.getSource().getSender();
        Player player = ctx.getArgument("target", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();

        String newRace = ERaces.getPlayerMng().getRandomRaceId();
        ERaces.getPlayerMng().setPlayerRace(player, newRace);
        RacesReloader.reloadRaceForPlayer(player);
        VisualsManager.updateVisualsForPlayer(player);
        String message = ERaces.getMsgMng().getString("commands.regenerate_player_race.regenerate_success");
        ChatUtil.message(sender, message, Map.of("{player}", player.getName(), "{race}", newRace));

        return Command.SINGLE_SUCCESS;
    }
}
