package dev.elysium.eraces.events;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.RacesReloader;
import dev.elysium.eraces.VisualsManager;
import dev.elysium.eraces.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Bukkit.getScheduler().runTaskLater(ERaces.getInstance(), () -> {
            Player player = event.getPlayer();
            RacesReloader.reloadRaceForPlayer(player);
            VisualsManager.updateVisualsForPlayer(player);
        }, 5L);
    }
}

