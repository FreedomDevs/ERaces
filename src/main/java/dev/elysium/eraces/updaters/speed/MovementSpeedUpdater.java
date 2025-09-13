package dev.elysium.eraces.updaters.speed;

import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.modifiers.ModifierAdapter;
import dev.elysium.eraces.modifiers.Modifiers;
import dev.elysium.eraces.updaters.base.IUpdater;
import org.bukkit.entity.Player;

public class MovementSpeedUpdater implements IUpdater {
    final ModifierAdapter mod = Modifiers.SPEED.register();

    @Override
    public void update(Race race, Player player) {
        mod.set(player, race.getMovementSpeedLevel());
    }
}
