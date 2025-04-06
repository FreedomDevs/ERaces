package dev.fdp.races;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dev.fdp.races.events.HeathUpdater;

public class RacesReloader {
  public static void reloadRaceForPlayer(Player player) {
    HeathUpdater.updateHealth(FDP_Races.getInstance().raceManager, player);
  }

  public static void reloadRaceForAllPlayers() {
    for (Player i : Bukkit.getOnlinePlayers()) {
      reloadRaceForPlayer(i);
    }
  }
}
