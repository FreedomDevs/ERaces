package dev.elysium.eraces.commands.executors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.elysium.eraces.items.RaceChangePotion;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import lombok.Getter;
import org.bukkit.entity.Player;

public class GetChangePotionExec {
    @Getter
    private final LiteralArgumentBuilder<CommandSourceStack> cmd;

    public GetChangePotionExec() {
        this.cmd = Commands.literal("get_change_potion")
                .requires(source -> source.getSender() instanceof Player)
                .executes(ctx -> {
                    ((Player) ctx.getSource().getSender()).getInventory().addItem(RaceChangePotion.createCustomPotion());
                    return Command.SINGLE_SUCCESS;
                });
    }
}
