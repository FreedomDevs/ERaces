package dev.fdp.races.updaters.damage;

import dev.fdp.races.datatypes.Race;
import dev.fdp.races.updaters.base.BaseDamageUpdater;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DualWeaponDamageUpdater extends BaseDamageUpdater {
    {
        OP = ADD;
    }

    @Override
    protected Double getAttribute(Race race) {
        return race.getWeaponProficiency().getDualWeaponDamageAdditional();
    }

    @Override
    protected boolean playerCheck(Player player) {
        Material t1 = player.getInventory().getItemInMainHand().getType();
        return t1 != Material.AIR && t1 == player.getInventory().getItemInOffHand().getType();
    }
}
