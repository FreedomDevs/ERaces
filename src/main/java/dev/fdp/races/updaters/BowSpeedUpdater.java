package dev.fdp.races.updaters;

import dev.fdp.races.datatypes.Race;
import dev.fdp.races.events.PlayerShootBowEvent;
import dev.fdp.races.updaters.base.RaceAttributeMapStorage;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BowSpeedUpdater extends RaceAttributeMapStorage<Double> implements Listener {
    @EventHandler
    public void onPlayerBowShoot(PlayerShootBowEvent event) {
        Double param = getParam(event.getPlayer());
        if (param == null) return;
        AbstractArrow arrow = event.getArrow();
        arrow.setVelocity(arrow.getVelocity().multiply(param));
    }

    @Override
    protected Double getAttribute(Race race) {
        return race.getWeaponProficiency().getBowProjectileSpeedMultiplier();
    }

    @Override
    protected boolean putCheck(Double att) {
        return att != 1;
    }
}
