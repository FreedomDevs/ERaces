package dev.fdp.races.commands;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.RacesReloader;
import dev.fdp.races.VisualsManager;
import dev.fdp.races.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class RegeneratePlayerRaceSubCommand extends AbstractSubCommand {
    public void onCommandExecute(CommandSender sender, String[] args) {
        String newRace = FDP_Races.getInstance().getPlayerDataManager().getRandomRace();

        if (!(sender instanceof Player) && args.length < 2) {
            String consoleError = FDP_Races.getInstance().getMessageManager().getString("commands.regenerate_player_race.console_usage");
            ChatUtil.message(sender, consoleError);
            return;
        }

        Player player = args.length == 1 ? (Player) sender : Bukkit.getPlayer(args[1]); // Если это вызовет консоль то бобо
        if (player == null) {
            String message = FDP_Races.getInstance().getMessageManager().getString("commands.regenerate_player_race.nick_error");
            ChatUtil.message(sender, message);
            return;
        }

        FDP_Races.getInstance().getPlayerDataManager().setPlayerRace(player.getName(), newRace);
        RacesReloader.reloadRaceForPlayer(player);
        VisualsManager.updateVisualsForPlayer(player);
        String message = FDP_Races.getInstance().getMessageManager().getString("commands.regenerate_player_race.regenerate_success");
        ChatUtil.message(sender, message,
                Map.of("{player}", player.getName(), "{race}", newRace));
    }
}
