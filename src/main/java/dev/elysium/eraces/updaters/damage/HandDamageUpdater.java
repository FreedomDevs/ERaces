package dev.elysium.eraces.updaters.damage;

import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.updaters.base.BaseDamageUpdater;
import dev.elysium.eraces.utils.ItemChecker;
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
