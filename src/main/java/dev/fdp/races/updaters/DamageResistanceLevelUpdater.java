package dev.fdp.races.updaters;

import dev.fdp.races.datatypes.Race;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DamageResistanceLevelUpdater implements IUpdater {

    @Override
    public void update(Race race, Player player) {
        int slowdownLevel = race.getSlowdownLevel();
        player.removePotionEffect(PotionEffectType.RESISTANCE);

        if (slowdownLevel != 0) {
            player.addPotionEffect(
                    new PotionEffect(
                            PotionEffectType.RESISTANCE,
                            Integer.MAX_VALUE,
                            (slowdownLevel - 1),
                            false,
                            false,
                            false));
        }
    }
}
