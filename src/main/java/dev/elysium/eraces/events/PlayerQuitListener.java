package dev.elysium.eraces.events;

import dev.elysium.eraces.RacesReloader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        RacesReloader.unloadPlayerData(event.getPlayer());
    }
}
