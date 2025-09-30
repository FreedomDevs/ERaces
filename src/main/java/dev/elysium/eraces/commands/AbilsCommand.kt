package dev.elysium.eraces.commands

import dev.elysium.eraces.ERaces
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AbilsCommand() : Command("abils") {
    init {
        description = "Использовать способности"
        usageMessage = "/abils <id>"
    }

    override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("§cТолько игрок может использовать способности")
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("§eИспользование: /abils <id>")
            return true
        }

        ERaces.getABM().activate(sender, args[0])
        return true
    }
}
