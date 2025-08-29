package dev.fdp.races.updaters;

import dev.fdp.races.FDP_Races;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExhaustionEvent;

public class SaturationUpdater implements Listener {

    @EventHandler
    public void onPlayerExhaust(EntityExhaustionEvent event) {
        double coeff = FDP_Races.getPlayerMng().getPlayerRace((Player) event.getEntity()).getExhaustionMultiplier();
        event.setExhaustion((float) (event.getExhaustion() * coeff));
    }
}
