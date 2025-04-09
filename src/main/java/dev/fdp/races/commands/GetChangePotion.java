package dev.fdp.races.commands;

import dev.fdp.races.items.RaceChangePotion;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetChangePotion extends AbstractSubCommand {
    @Override
    public void onCommandExecute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        player.getInventory().addItem(RaceChangePotion.createCustomPotion());
    }
}
