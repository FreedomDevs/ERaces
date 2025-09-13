package dev.elysium.eraces.updaters.damage;

import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.updaters.base.BaseDamageUpdater;
import dev.elysium.eraces.utils.ArmorChecker;
import org.bukkit.entity.Player;

public class DamageAdditionalWithLowerThanIronArmorUpdater extends BaseDamageUpdater {
    {
        OP = ADD;
    }

    @Override
    protected Double getAttribute(Race race) {
        return race.getWeaponProficiency().getDamageAdditionalWithIronAndLowerArmor();
    }

    @Override
    protected boolean playerCheck(Player player) {
        return ArmorChecker.allArmorLess(player, ArmorChecker.ArmorType.IRON);
    }
}
