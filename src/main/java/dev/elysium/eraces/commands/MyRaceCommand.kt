package dev.elysium.eraces.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import dev.elysium.eraces.ERaces.Companion.getInstance
import dev.elysium.eraces.utils.msg
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.entity.Player

object MyRaceCommand {
    val cmd: LiteralCommandNode<CommandSourceStack> =
        Commands.literal("myrace").requires { source: CommandSourceStack -> source.sender is Player }
            .executes { ctx: CommandContext<CommandSourceStack> ->
                myRaceCommandLogic(ctx.getSource().sender as Player)
                Command.SINGLE_SUCCESS
            }.build()


    private fun myRaceCommandLogic(player: Player) {
        val messages = getInstance().context.messageManager.data
        val playerRace = getInstance().context.playerDataManager.getPlayerRaceId(player)

        if (playerRace.isNullOrEmpty()) {
            player.msg(messages.raceNotSelected)
            return
        }

        val message = messages.getPlayerRaceSuccessMe
        player.msg(message, Pair("{race}", playerRace))
    }
}