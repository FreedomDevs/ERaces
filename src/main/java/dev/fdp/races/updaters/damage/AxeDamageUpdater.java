package dev.fdp.races.updaters.damage;

import dev.fdp.races.datatypes.Race;
import dev.fdp.races.updaters.base.BaseDamageUpdater;
import dev.fdp.races.utils.ItemChecker;
import org.bukkit.entity.Player;

public class AxeDamageUpdater extends BaseDamageUpdater {
    @Override
    protected boolean playerCheck(Player player) {
        return ItemChecker.isToolType(player.getInventory().getItemInMainHand().getType(), ItemChecker.ToolTypes.AXE);
    }

    @Override
    protected Double getAttribute(Race race) {
        return race.getWeaponProficiency().getAxeDamageMultiplier();
    }
}
