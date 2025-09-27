package dev.elysium.eraces.commands.executors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.utils.ChatUtil;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class GetPlayerRaceExec {
    @Getter
    private final LiteralArgumentBuilder<CommandSourceStack> cmd;

    public GetPlayerRaceExec() {
        this.cmd = Commands.literal("get_player_race")
                .then(Commands.argument("target", ArgumentTypes.player()).executes(this::exec));
    }

    private int exec(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSender sender = ctx.getSource().getSender();
        Player player = ctx.getArgument("target", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();
        String playerName = player.getName();

        String playerRace = ERaces.getPlayerMng().getPlayerRaceId(player);

        if (sender.getName().equals(playerName)) {
            String message = ERaces.getMsgMng().getGetPlayerRaceSuccessMe();
            ChatUtil.message(sender, message,
                    Map.of("{race}", playerRace));
        } else {
            String message = ERaces.getMsgMng().getGetPlayerRaceSuccess();
            ChatUtil.message(sender, message,
                    Map.of("{player}", playerName, "{race}", playerRace));
        }

        return Command.SINGLE_SUCCESS;
    }
}
