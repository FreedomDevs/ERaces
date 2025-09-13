package dev.elysium.eraces.updaters.damage;

import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.updaters.base.BaseDamageUpdater;
import dev.elysium.eraces.utils.ItemChecker;
import org.bukkit.entity.Player;

public class MaceDamageUpdater extends BaseDamageUpdater {
    @Override
    protected Double getAttribute(Race race) {
        return race.getWeaponProficiency().getMaceDamageMultiplier();
    }

    @Override
    protected boolean playerCheck(Player player) {
        return ItemChecker.isToolType(player.getInventory().getItemInMainHand().getType(), ItemChecker.ToolTypes.MACE);
    }
}
