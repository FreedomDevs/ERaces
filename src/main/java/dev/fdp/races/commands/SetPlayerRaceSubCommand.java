package dev.fdp.races.commands;

import dev.fdp.races.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.RacesReloader;

import java.util.Map;

public class SetPlayerRaceSubCommand extends AbstractSubCommand {
  public void onCommandExecute(CommandSender sender, String[] args) {
    if (args.length < 3) {
      ChatUtil.message(sender, "<red>Мала аргументов");
      return;
    }

    String newRace = args[2];

    if (!FDP_Races.getInstance().races.containsKey(newRace)) {
      ChatUtil.message(sender, "<red>Такая расса не существует");
      return;
    }

    Player player = Bukkit.getPlayer(args[1]);
    if (player == null) {
      ChatUtil.message(sender, "<red>Неверно указан ник игрока");
      return;
    }

    FDP_Races.getInstance().raceManager.setPlayerRace(player.getName(), newRace);
    RacesReloader.reloadRaceForPlayer(player);
    ChatUtil.message(sender, "<green>Расса игрока <yellow>{player} <green>установлена на: <gold>{race}", Map.of("{player}", player.getName(), "{race}", newRace));
  }
}
