package dev.fdp.races.events;

import dev.fdp.races.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Bukkit.getScheduler().runTaskLater(FDP_Races.getInstance(), () -> {
            Player player = event.getPlayer();
            RacesReloader.reloadRaceForPlayer(player);
            VisualsManager.updateVisualsForPlayer(player);
        }, 5L);
    }
}

