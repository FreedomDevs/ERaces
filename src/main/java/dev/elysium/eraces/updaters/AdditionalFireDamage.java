package dev.elysium.eraces.updaters;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.datatypes.Race;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class AdditionalFireDamage implements Listener {
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;

        if (!(event.getCause() == EntityDamageEvent.DamageCause.CAMPFIRE || event.getCause() == EntityDamageEvent.DamageCause.MELTING || event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.LAVA || event.getCause() == EntityDamageEvent.DamageCause.HOT_FLOOR))
            return;

        Race race = ERaces.getInstance().getContext().getPlayerDataManager().getPlayerRace((Player) event.getEntity());
        event.setDamage(event.getDamage() + race.getAdditionalFireDamage());
    }
}
