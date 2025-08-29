package dev.fdp.races.updaters.damage;

import dev.fdp.races.datatypes.Race;
import dev.fdp.races.updaters.base.BaseDamageUpdater;
import dev.fdp.races.utils.ItemChecker;
import org.bukkit.entity.Player;

public class HandDamageUpdater extends BaseDamageUpdater {
    {
        OP = ADD;
    }

    @Override
    protected boolean playerCheck(Player player) {
        return !ItemChecker.isTool(player.getInventory().getItemInMainHand().getType());
    }

    @Override
    protected Double getAttribute(Race race) {
        return race.getWeaponProficiency().getHandDamageAdditional();
    }
}
