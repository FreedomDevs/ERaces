package dev.fdp.races.updaters.speed;

import dev.fdp.races.datatypes.Race;
import dev.fdp.races.modifiers.ModifierAdapter;
import dev.fdp.races.modifiers.Modifiers;
import dev.fdp.races.updaters.base.IUpdater;
import org.bukkit.entity.Player;

public class MovementSpeedUpdater implements IUpdater {
    final ModifierAdapter mod = Modifiers.SPEED.register();

    @Override
    public void update(Race race, Player player) {
        mod.set(player, race.getMovementSpeedLevel());
    }
}
