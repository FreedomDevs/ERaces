package dev.elysium.eraces.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.utils.ChatUtil;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Map;

public class MyraceCommand {
    @Getter
    private final LiteralCommandNode<CommandSourceStack> cmd;

    public MyraceCommand() {
        this.cmd = Commands.literal("myrace")
                .executes(ctx -> {
                    Entity executor = ctx.getSource().getExecutor();
                    if (!(executor instanceof Player player)) {
                        ChatUtil.message(executor, "Эту команду может использовать только игрок.");
                        return Command.SINGLE_SUCCESS;
                    }
                    String playerRace = ERaces.getPlayerMng().getPlayerRaceId(player);

                    if (playerRace == null || playerRace.isEmpty()) {
                        ChatUtil.message(player, "У вас не выбрана раса.");
                        return Command.SINGLE_SUCCESS;
                    }

                    String message = ERaces.getMsgMng().getGetPlayerRaceSuccessMe();
                    ChatUtil.message(player, message, Map.of("{race}", playerRace));
                    return Command.SINGLE_SUCCESS;
                }).build();
    }
}
