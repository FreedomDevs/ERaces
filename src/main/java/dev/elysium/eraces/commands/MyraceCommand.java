package dev.elysium.eraces.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.utils.ChatUtil;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Map;

public class MyraceCommand {
    @Getter
    private final LiteralCommandNode<CommandSourceStack> cmd;

    public MyraceCommand() {
        this.cmd = Commands.literal("myrace")
                .requires(source -> source.getSender() instanceof Player)
                .executes(ctx -> {
                    myraceCommandLogic((Player) ctx.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                }).build();
    }

    private static void myraceCommandLogic(Player player) {
        var context = ERaces.getInstance().getContext();

        String playerRace = context.getPlayerDataManager().getPlayerRaceId(player);

        if (playerRace == null || playerRace.isEmpty()) {
            ChatUtil.message(player, context.getMessageManager().getData().getRaceNotSelected());
            return;
        }

        String message = context.getMessageManager().getData().getGetPlayerRaceSuccessMe();
        ChatUtil.message(player, message, Map.of("{race}", playerRace));
    }
}

