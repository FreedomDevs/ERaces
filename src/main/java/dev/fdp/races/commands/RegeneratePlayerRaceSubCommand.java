package dev.fdp.races.commands;

import dev.fdp.races.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.RacesReloader;

import java.util.Map;

public class RegeneratePlayerRaceSubCommand extends AbstractSubCommand {
  public void onCommandExecute(CommandSender sender, String[] args) {
    String newRace = FDP_Races.getInstance().raceManager.getRandomRace();

    Player player = args.length == 1 ? (Player) sender : Bukkit.getPlayer(args[1]); // Если это вызовет консоль то бобо
    if (player == null) {
      ChatUtil.message(sender, "<red>Неверно указан ник игрока");
      return;
    }

    FDP_Races.getInstance().raceManager.setPlayerRace(player.getName(), newRace);
    RacesReloader.reloadRaceForPlayer(player);
    ChatUtil.message(sender, "<green>Расса игрока <yellow>{player} <green>установлена на: <gold>{race}", Map.of("{player}", player.getName(), "{race}", newRace));
  }
}
