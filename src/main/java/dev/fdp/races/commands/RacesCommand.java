package dev.fdp.races.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.RacesReloader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class RacesCommand implements TabExecutor {
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0) {
      sender.sendMessage(Component.text("Не указана подкоманда", NamedTextColor.RED));
    }

    if (args[0].equals("reload")) {
      FDP_Races.getInstance().reloadConfig();
      FDP_Races.getInstance().raceManager.loadData();
      RacesReloader.reloadRaceForAllPlayers();
      sender.sendMessage(Component.text("Конфиг перезагружен👍"));
    }
    return true;
  }

  public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 1) {
      return Arrays.asList("reload");
    }
    return Collections.emptyList();
  }
}
