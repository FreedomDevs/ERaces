package dev.elysium.eraces.updaters;

import dev.elysium.eraces.ERaces;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExhaustionEvent;

public class SaturationUpdater implements Listener {

    @EventHandler
    public void onPlayerExhaust(EntityExhaustionEvent event) {
        double coeff = ERaces.getPlayerMng().getPlayerRace((Player) event.getEntity()).getExhaustionMultiplier();
        event.setExhaustion((float) (event.getExhaustion() * coeff));
    }
}
