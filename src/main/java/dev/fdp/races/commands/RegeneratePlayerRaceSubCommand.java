package dev.fdp.races.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.RacesReloader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class RegeneratePlayerRaceSubCommand extends AbstractSubCommand {
  public void onCommandExecute(CommandSender sender, String[] args) {
    String newRace = FDP_Races.getInstance().raceManager.getRandomRace();

    Player player = args.length == 1 ? (Player) sender : Bukkit.getPlayer(args[1]); // Если это вызовет консоль то бобо
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
