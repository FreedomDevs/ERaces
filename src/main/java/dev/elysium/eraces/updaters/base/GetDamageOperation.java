package dev.elysium.eraces.updaters.base;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface GetDamageOperation {
    Double getDamage(Double initDamage, Double param, Player player);
}
