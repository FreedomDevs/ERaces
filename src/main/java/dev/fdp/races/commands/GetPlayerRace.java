package dev.fdp.races.commands;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.utils.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class GetPlayerRace extends AbstractSubCommand {
    public void onCommandExecute(CommandSender sender, String[] args) {
        if (args.length < 2 && !(sender instanceof Player)) {
            String message = FDP_Races.getInstance().getMessageManager().getString("commands.get_player_race.race_check_error");
            ChatUtil.message(sender, message);
            return;
        }

        String playerName = args.length < 2 ? sender.getName() : args[1];

        String playerRace = FDP_Races.getInstance().getPlayerDataManager().getPlayerRace(playerName);

        if (sender.getName().equals(playerName)) {
            String message = FDP_Races.getInstance().getMessageManager().getString("commands.get_player_race.race_check_success_me");
            ChatUtil.message(sender, message,
                    Map.of("{race}", playerRace));
        } else {
            String message = FDP_Races.getInstance().getMessageManager().getString("commands.get_player_race.race_check_success");
            ChatUtil.message(sender, message,
                    Map.of("{player}", playerName, "{race}", playerRace));
        }

    }
}
