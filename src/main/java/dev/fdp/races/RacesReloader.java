package dev.fdp.races;

import dev.fdp.races.events.AdditionalArmorUpdater;
import dev.fdp.races.events.AttackRangeUpdater;
import dev.fdp.races.events.MineSpeedUpdater;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dev.fdp.races.events.HeathUpdater;

public class RacesReloader {
  public static void reloadRaceForPlayer(Player player) {
    HeathUpdater.updateHealth(FDP_Races.getInstance().raceManager, player);
    MineSpeedUpdater.updateMineSpeed(FDP_Races.getInstance().raceManager, player);
    AttackRangeUpdater.updateAttackRange(FDP_Races.getInstance().raceManager, player);
    AdditionalArmorUpdater.updateAdditionalArmor(FDP_Races.getInstance().raceManager, player);
  }

  public static void reloadRaceForAllPlayers() {
    for (Player i : Bukkit.getOnlinePlayers()) {
      reloadRaceForPlayer(i);
    }
  }
}
