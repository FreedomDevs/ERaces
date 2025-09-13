package dev.fdp.races.updaters.damage;

import dev.fdp.races.datatypes.Race;
import dev.fdp.races.updaters.base.BaseDamageUpdater;
import dev.fdp.races.utils.ArmorChecker;
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
