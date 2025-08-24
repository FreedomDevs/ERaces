package dev.fdp.races.updaters;

import dev.fdp.races.datatypes.Race;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DamageResistanceLevelUpdater implements IUpdater {

    @Override
    public void update(Race race, Player player) {
        int slowdownLevel = race.getDamageResistanceLevel();
        player.removePotionEffect(PotionEffectType.RESISTANCE);

        if (slowdownLevel != 0) {
            PotionEffectType.RESISTANCE.createEffect(PotionEffect.INFINITE_DURATION, slowdownLevel - 1).apply(player);
        }
    }
}
