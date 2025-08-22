package dev.fdp.races.commands.executors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.fdp.races.items.RaceChangePotion;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.Getter;
import org.bukkit.entity.Player;

public class GetChangePotionExec {
    @Getter
    private final LiteralArgumentBuilder<CommandSourceStack> cmd;

    public GetChangePotionExec() {
        this.cmd = Commands.literal("get_change_potion")
                .executes(ctx -> {
                    ((Player) ctx.getSource().getSender()).getInventory().addItem(RaceChangePotion.createCustomPotion());
                    return Command.SINGLE_SUCCESS;
                });
    }
}
