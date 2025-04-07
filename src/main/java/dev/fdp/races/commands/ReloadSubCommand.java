package dev.fdp.races.commands;

import dev.fdp.races.utils.ChatUtil;
import org.bukkit.command.CommandSender;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.RacesReloader;

public class ReloadSubCommand extends AbstractSubCommand {
  public void onCommandExecute(CommandSender sender, String[] args) {
    FDP_Races.getInstance().reloadConfig();
    FDP_Races.getInstance().raceManager.loadData();
    RacesReloader.reloadRaceForAllPlayers();
    String message = FDP_Races.getInstance().messageManager.getString("commands.reload.reload_success");
    ChatUtil.message(sender, message);
  }
}
