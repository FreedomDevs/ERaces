package dev.fdp.races.updaters;

import org.bukkit.entity.Player;

import dev.fdp.races.Race;

public interface IUpdater {
  public void update(Race race, Player player);
}
