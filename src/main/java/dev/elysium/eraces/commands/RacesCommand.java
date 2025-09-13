package dev.elysium.eraces.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.elysium.eraces.commands.executors.*;
import dev.elysium.eraces.commands.executors.*;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.Getter;

public class RacesCommand {
    @Getter
    private final LiteralCommandNode<CommandSourceStack> cmd;

    public RacesCommand() {
        this.cmd = Commands.literal("races")
                .requires(sender -> sender.getSender().hasPermission("fdp_races.races_command.use"))
                .then(new ReloadExec().getCmd())
                .then(new RegeneratePlayerRaceExec().getCmd())
                .then(new GetPlayerRaceExec().getCmd())
                .then(new SetPlayerRaceExec().getCmd())
                .then(new GetChangePotionExec().getCmd())
                .build();
    }
}
