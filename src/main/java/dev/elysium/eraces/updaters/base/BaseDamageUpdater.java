package dev.elysium.eraces.updaters.base;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public abstract class BaseDamageUpdater extends RaceAttributeMapStorage<Double> implements Listener {
    protected static final GetDamageOperation ADD = (a, b, c) -> a + b;
    protected static final GetDamageOperation MULTIPLY = (a, b, c) -> a * b;

    protected GetDamageOperation OP = MULTIPLY;

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            Double param = getParam(player);
            if (param == null || !playerCheck(player)) return;
            event.setDamage(OP.getDamage(event.getDamage(), param, player));
        }
    }

    protected abstract boolean playerCheck(Player player);

    @Override
    protected boolean putCheck(Double att) {
        return OP == ADD ? att != 0 : att != 1;
    }
}
