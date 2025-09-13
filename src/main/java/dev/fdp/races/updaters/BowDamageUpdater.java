package dev.fdp.races.updaters;

import dev.fdp.races.datatypes.Race;
import dev.fdp.races.events.PlayerShootBowEvent;
import dev.fdp.races.updaters.base.RaceAttributeMapStorage;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BowDamageUpdater extends RaceAttributeMapStorage<Double> implements Listener {
    @EventHandler
    public void onPlayerBowShoot(PlayerShootBowEvent event) {
        Double param = getParam(event.getPlayer());
        if (param == null) return;
        AbstractArrow arrow = event.getArrow();
        arrow.setDamage(arrow.getDamage() * param);
    }

    @Override
    protected Double getAttribute(Race race) {
        return race.getWeaponProficiency().getBowDamageMultiplier();
    }

    @Override
    protected boolean putCheck(Double att) {
        return att != 1;
    }
}
