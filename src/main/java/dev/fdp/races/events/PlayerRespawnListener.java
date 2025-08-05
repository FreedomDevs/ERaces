package dev.fdp.races.events;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.RacesReloader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Bukkit.getScheduler().runTaskLater(FDP_Races.getInstance(), () -> {
                RacesReloader.reloadRaceForPlayer(event.getPlayer());
            }, 5L
        );
    }
}
