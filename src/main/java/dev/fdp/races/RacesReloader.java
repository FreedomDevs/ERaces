package dev.fdp.races;

import dev.fdp.races.updaters.*;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RacesReloader {

  private static final List<IUpdater> updaters = List.of(
      // new AttackRangeUpdater(),
      new HealthUpdater(),
      new MineSpeedUpdater(),
      new AdditionalArmorUpdater(),
      new HealthRegenUpdater(),
      new ShieldUsageUpdater(),
      new ForbiddenFoodsUpdater(),
      new HandDamageUpdater(),
      new RunningSpeedUpdater(),
      new AntiKnockbackLevelUpdater(),
      new BiomeSpeedUpdater(),
      new AxeDamageUpdater(),
      new SwordDamageUpdater(),
      new BowDamageUpdater()
  );

  public static void reloadRaceForPlayer(Player player) {
    RaceManager raceManager = FDP_Races.getInstance().raceManager;

    for (IUpdater updater : updaters) {
      updater.update(raceManager.getRaces().get(raceManager.getPlayerRace(player.getName())), player);
    }
  }

  public static void reloadRaceForAllPlayers() {
    for (Player i : Bukkit.getOnlinePlayers()) {
      reloadRaceForPlayer(i);
    }
  }
}
