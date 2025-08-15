package dev.fdp.races.updaters;

import dev.fdp.races.Race;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MineSpeedUpdater implements IUpdater {
    // TODO: переписать через атрибут
    @Override
    public void update(Race race, Player player) {
        double mineSpeed = race.getMineSpeed();
        player.removePotionEffect(PotionEffectType.HASTE);

        if (mineSpeed > 1.0) {
            int hasteLevel = (int) Math.round((mineSpeed - 1.0) / 0.2);

            player.addPotionEffect(
                    new PotionEffect(
                            PotionEffectType.HASTE,
                            Integer.MAX_VALUE,
                            hasteLevel - 1,
                            false,
                            false,
                            false
                    )
            );
        }
    }
}
