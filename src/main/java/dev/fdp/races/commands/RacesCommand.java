package dev.fdp.races.commands;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.fdp.races.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import dev.fdp.races.FDP_Races;

public class RacesCommand implements TabExecutor {
  Map<String, AbstractSubCommand> subCommands;

  public RacesCommand() {
    subCommands = new HashMap<>();
    subCommands.put("reload", new ReloadSubCommand());
    subCommands.put("regenerate_player_race", new RegeneratePlayerRaceSubCommand());
    subCommands.put("set_player_race", new SetPlayerRaceSubCommand());
    subCommands.put("get_player_race", new GetPlayerRace());
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0) {
      ChatUtil.message(sender, "<red>Не указана подкоманда");
    }

    if (!subCommands.containsKey(args[0])) {
      ChatUtil.message(sender, "<red>Подкоманда: <yellow>{arg} <red>не существует", Map.of("{arg}", args[0]));
    }

    subCommands.get(args[0]).onCommandExecute(sender, args);
    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 1) {
      String input = args[0].toLowerCase();
      return subCommands.keySet().stream()
          .filter(s -> s.toLowerCase().startsWith(input))
          .sorted()
          .toList();
    }

    if (args.length == 2) {
      String sub = args[0].toLowerCase();
      if (sub.equals("set_player_race") || sub.equals("regenerate_player_race") || sub.equals("get_player_race")) {
        String input = args[1].toLowerCase();
        return Bukkit.getOnlinePlayers().stream()
            .map(player -> player.getName())
            .filter(name -> name.toLowerCase().startsWith(input))
            .sorted()
            .toList();
      }
    }

    if (args.length == 3) {
      String sub = args[0].toLowerCase();
      if (sub.equals("set_player_race")) {
        String input = args[2].toLowerCase();
        return FDP_Races.getInstance().races.keySet().stream()
            .filter(s -> s.toLowerCase().startsWith(input))
            .sorted()
            .toList();
      }
    }

    return Collections.emptyList();
  }

}
