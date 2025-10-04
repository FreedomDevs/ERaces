package dev.elysium.eraces.commands.executors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.RacesReloader;
import dev.elysium.eraces.VisualsManager;
import dev.elysium.eraces.utils.ChatUtil;
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
        ERaces.getRacesMng().reloadConfig();
        RacesReloader.reloadRaceForAllPlayers();
        VisualsManager.reloadVisualsForAllPlayer();

        String message = ERaces.getMsgMng().getReloadSuccess();
        ChatUtil.message(ctx.getSource().getSender(), message);
        return Command.SINGLE_SUCCESS;
    }
}
