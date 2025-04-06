package dev.fdp.races.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.RacesReloader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class SetPlayerRaceSubCommand extends AbstractSubCommand {
  public void onCommandExecute(CommandSender sender, String[] args) {
    if (args.length < 3) {
      sender.sendMessage(Component.text("Мала аргументов", NamedTextColor.RED));
      return;
    }

    String newRace = args[2];

    if (!FDP_Races.getInstance().races.containsKey(newRace)) {
      sender.sendMessage(Component.text("Такая расса не существует", NamedTextColor.RED));
      return;
    }

    Player player = Bukkit.getPlayer(args[1]);
    if (player == null) {
      sender.sendMessage(Component.text("Неверно указан ник игрока", NamedTextColor.RED));
      return;
    }

    FDP_Races.getInstance().raceManager.setPlayerRace(player.getName(), newRace);
    RacesReloader.reloadRaceForPlayer(player);
    sender.sendMessage(
        Component.text("Расса игрока " + player.getName() + " установлена расса: " + newRace, NamedTextColor.GREEN));
  }
}
