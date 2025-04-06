package dev.fdp.races.commands;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.utils.ChatUtil;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class GetPlayerRace extends AbstractSubCommand {
    public void onCommandExecute(CommandSender sender, String[] args) {
        String playerName = args[1];
        String playerRace = FDP_Races.getInstance().raceManager.getPlayerRace(playerName);
        ChatUtil.message(sender, "<green>У игрока <yellow>{player} <green>расса <yellow>{race}", Map.of("{player}", playerName, "{race}", playerRace));
    }
}
