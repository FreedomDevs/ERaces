package dev.fdp.races.updaters;

import org.bukkit.entity.Player;

import dev.fdp.races.RaceManager;

public interface IUpdater {
  public void update(RaceManager raceManager, Player player);
}
