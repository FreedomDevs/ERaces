package dev.fdp.races.updaters.damage;

import dev.fdp.races.datatypes.Race;
import dev.fdp.races.updaters.base.BaseDamageUpdater;
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
