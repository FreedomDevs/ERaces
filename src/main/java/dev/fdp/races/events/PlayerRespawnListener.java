package dev.fdp.races.events;

import dev.fdp.races.RacesReloader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        RacesReloader.reloadRaceForPlayer(event.getPlayer());
    }
}
