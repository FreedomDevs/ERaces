package dev.fdp.races.commands;

import dev.fdp.races.*;
import dev.fdp.races.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class SetPlayerRaceSubCommand extends AbstractSubCommand {
  public void onCommandExecute(CommandSender sender, String[] args) {
    if (args.length < 3) {
      String message = FDP_Races.getInstance().messageManager.getString("commands.set_player_race.small_args");
      ChatUtil.message(sender, message);
      return;
    }

    String newRace = args[2];

    if (!FDP_Races.getInstance().races.containsKey(newRace)) {
      String message = FDP_Races.getInstance().messageManager.getString("commands.set_player_race.race_not_found");
      ChatUtil.message(sender, message);
      return;
    }

    Player player = Bukkit.getPlayer(args[1]);
    if (player == null) {
      String message = FDP_Races.getInstance().messageManager.getString("commands.set_player_race.nick_null");
      ChatUtil.message(sender, message);
      return;
    }

    FDP_Races.getInstance().raceManager.setPlayerRace(player.getName(), newRace);
    RacesReloader.reloadRaceForPlayer(player);
    VisualsManager.updateVisualsForPlayer(player);
    String message = FDP_Races.getInstance().messageManager.getString("commands.set_player_race.set_success");
    ChatUtil.message(sender, message,
        Map.of("{player}", player.getName(), "{race}", newRace));
  }
}
