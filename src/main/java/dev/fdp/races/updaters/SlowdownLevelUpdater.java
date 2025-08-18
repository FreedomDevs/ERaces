package dev.fdp.races.updaters;

import dev.fdp.races.datatypes.Race;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SlowdownLevelUpdater implements IUpdater {
    // TODO: объединить с RunningSpeed
    @Override
    public void update(Race race, Player player) {
        int slowdownLevel = race.getSlowdownLevel();
        player.removePotionEffect(PotionEffectType.SLOWNESS);

        if (slowdownLevel != 0) {
            player.addPotionEffect(
                    new PotionEffect(
                            PotionEffectType.SLOWNESS,
                            Integer.MAX_VALUE,
                            (slowdownLevel - 1),
                            false,
                            false,
                            false));
        }
    }
}
