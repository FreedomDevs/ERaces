package dev.elysium.eraces.updaters.damage;

import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.updaters.base.BaseDamageUpdater;
import dev.elysium.eraces.utils.ItemChecker;
import org.bukkit.entity.Player;

public class SwordDamageUpdater extends BaseDamageUpdater {
    @Override
    protected Double getAttribute(Race race) {
        return race.getWeaponProficiency().getSwordDamageMultiplier();
    }

    @Override
    protected boolean playerCheck(Player player) {
        return ItemChecker.isToolType(player.getInventory().getItemInMainHand().getType(), ItemChecker.ToolTypes.SWORD);
    }
}
