package dev.fdp.races.events;

import dev.fdp.races.RacesReloader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        RacesReloader.unloadPlayerData(event.getPlayer());
    }
}
