package dev.fdp.races.commands;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.utils.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class GetPlayerRace extends AbstractSubCommand {
    public void onCommandExecute(CommandSender sender, String[] args) {
        if (args.length < 2 && !(sender instanceof Player)) {
            ChatUtil.message(sender, "<red>Вы не указали игрока для получения расы");
            return;
        }

        String playerName = args.length < 2 ? sender.getName() : args[1];

        String playerRace = FDP_Races.getInstance().raceManager.getPlayerRace(playerName);

        if (sender.getName().equals(playerName))
            ChatUtil.message(sender, "<green>Ваша раса <yellow>{race}",
                    Map.of("{race}", playerRace));
        else
            ChatUtil.message(sender, "<green>У игрока <yellow>{player} <green>раса <yellow>{race}",
                    Map.of("{player}", playerName, "{race}", playerRace));
    }
}
