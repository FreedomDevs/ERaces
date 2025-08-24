package dev.fdp.races.updaters;

import dev.fdp.races.datatypes.Race;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MineSpeedUpdater implements IUpdater {
    @Override
    public void update(Race race, Player player) {
        int hasteLevel = race.getHasteLevel();
        player.removePotionEffect(PotionEffectType.HASTE);

        if (hasteLevel > 1) {
            PotionEffectType.HASTE.createEffect(PotionEffect.INFINITE_DURATION, hasteLevel - 1).apply(player);
        }
    }
}
