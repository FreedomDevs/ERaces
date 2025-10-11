package dev.elysium.eraces.commands.executors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.elysium.eraces.ERaces;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import lombok.Getter;
import org.bukkit.entity.Player;

public class AddXpExec {
    @Getter
    private final LiteralArgumentBuilder<CommandSourceStack> cmd;

    public AddXpExec() {
        this.cmd = Commands.literal("xp_add")
                .then(Commands.argument("target", ArgumentTypes.player()))
                .then(Commands.argument("count", LongArgumentType.longArg(1, Long.MAX_VALUE)))
                .executes(this::exec);
    }

    private int exec(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Player player = ctx.getArgument("target", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();
        long count = ctx.getArgument("count", long.class);

        ERaces.getSpecializationMng().addXp(player, count);
        return Command.SINGLE_SUCCESS;
    }
}
