package dev.fdp.races.events;

import dev.fdp.races.FDP_Races;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExhaustionEvent;

public class SaturationUpdater implements Listener {

    @EventHandler
    public void onPlayerExhaust(EntityExhaustionEvent event) {
        double coeff = FDP_Races.getInstance().getPlayerDataManager().getPlayerRace(event.getEntity().getName()).getExhaustionMultiplier();
        event.setExhaustion((float) (event.getExhaustion() * coeff));
    }
}
