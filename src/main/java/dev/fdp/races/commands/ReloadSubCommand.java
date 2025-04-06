package dev.fdp.races.commands;

import org.bukkit.command.CommandSender;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.RacesReloader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ReloadSubCommand extends AbstractSubCommand {
  public void onCommandExecute(CommandSender sender, String[] args) {
    FDP_Races.getInstance().reloadConfig();
    FDP_Races.getInstance().raceManager.loadData();
    RacesReloader.reloadRaceForAllPlayers();
    sender.sendMessage(Component.text("Конфиг перезагружен👍", NamedTextColor.GREEN));
  }
}
