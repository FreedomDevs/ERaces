package dev.fdp.races.updaters;

import dev.fdp.races.datatypes.Race;
import dev.fdp.races.updaters.base.IUpdater;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DamageResistanceLevelUpdater implements IUpdater {

    @Override
    public void update(Race race, Player player) {
        int resistanceLevel = race.getDamageResistanceLevel();
        player.removePotionEffect(PotionEffectType.RESISTANCE);

        if (resistanceLevel != 0) {
            PotionEffectType.RESISTANCE.createEffect(PotionEffect.INFINITE_DURATION, resistanceLevel - 1).apply(player);
        }
    }
}
