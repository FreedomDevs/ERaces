package dev.elysium.eraces.updaters.damage;

import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.updaters.base.BaseDamageUpdater;
import org.bukkit.entity.Player;

public class DamageUpdater extends BaseDamageUpdater {
    {
        OP = ADD;
    }

    @Override
    protected boolean playerCheck(Player player) {
        return true;
    }

    @Override
    protected Double getAttribute(Race race) {
        return race.getWeaponProficiency().getDamageAdditional();
    }

}
