package dev.fdp.races.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.fdp.races.FDP_Races;
import dev.fdp.races.utils.ChatUtil;
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
                    String playerRace = FDP_Races.getPlayerMng().getPlayerRaceId(player);

                    if (playerRace == null || playerRace.isEmpty()) {
                        ChatUtil.message(player, "У вас не выбрана раса.");
                        return Command.SINGLE_SUCCESS;
                    }

                    String message = FDP_Races.getMsgMng().getString("commands.get_player_race.race_check_success_me");
                    ChatUtil.message(player, message, Map.of("{race}", playerRace));
                    return Command.SINGLE_SUCCESS;
                }).build();
    }
}
