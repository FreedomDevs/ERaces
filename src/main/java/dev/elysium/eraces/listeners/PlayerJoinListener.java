package dev.elysium.eraces.listeners;

import dev.elysium.eraces.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ERaces.getInstance().getContext().getSpecializationsManager().ensurePlayerInitialized(event.getPlayer());
        ERaces.getInstance().getContext().getManaManager().getMana(player);
    }
}
