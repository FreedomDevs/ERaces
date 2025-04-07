package dev.fdp.races.updaters;

import dev.fdp.races.Race;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RunningSpeedUpdater implements IUpdater {

    @Override
    public void update(Race race, Player player) {
        int runningSpeed = race.getRunningSpeed();
        player.removePotionEffect(PotionEffectType.SPEED);

        if (runningSpeed > 0) {
            player.addPotionEffect(
                    new PotionEffect(
                            PotionEffectType.SPEED,
                            Integer.MAX_VALUE,
                            runningSpeed,
                            false,
                            false,
                            false));
        }
    }
}
