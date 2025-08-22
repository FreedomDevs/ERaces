package dev.fdp.races.commands.executors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.fdp.races.FDP_Races;
import dev.fdp.races.RacesReloader;
import dev.fdp.races.VisualsManager;
import dev.fdp.races.utils.ChatUtil;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.Getter;

public class ReloadExec {
    @Getter
    private final LiteralArgumentBuilder<CommandSourceStack> cmd;

    public ReloadExec() {
        this.cmd = Commands.literal("reload").executes(this::exec);
    }

    private int exec(CommandContext<CommandSourceStack> ctx) {
        FDP_Races.getInstance().getRacesConfigManager().reloadConfig();
        FDP_Races.getInstance().getPlayerDataManager().loadData();
        RacesReloader.reloadRaceForAllPlayers();
        VisualsManager.reloadVisualsForAllPlayer();
        String message = FDP_Races.getInstance().getMessageManager().getString("commands.reload.reload_success");
        ChatUtil.message(ctx.getSource().getSender(), message);
        return Command.SINGLE_SUCCESS;
    }
}
