package dev.elysium.eraces.events;

import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class PlayerShootBowEventListener implements Listener {
    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player player && event.getBow() != null) {
            if (event.getBow().getType() == Material.BOW && event.getProjectile() instanceof AbstractArrow arrow) {
                new PlayerShootBowEvent(player, arrow).callEvent();
            }
        }
    }
}
