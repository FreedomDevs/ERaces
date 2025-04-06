package dev.fdp.races.events;

import dev.fdp.races.RaceManager;
import dev.fdp.races.RacesReloader;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final RaceManager raceManager;

    public PlayerJoinListener(RaceManager raceManager) {
        this.raceManager = raceManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!raceManager.getNameToRaceMap().containsKey(event.getPlayer().getName().toLowerCase())) {
            String randomRace = raceManager.getRandomRace();

            raceManager.setPlayerRace(player.getName(), randomRace);
        }

        RacesReloader.reloadRaceForPlayer(player);
    }
}
