package dev.elysium.eraces.updaters;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.datatypes.EffectsWith;
import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.updaters.base.IUpdater;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class EffectsUpdater implements IUpdater {
    @Override
    public void update(Race race, Player player) {
        EffectsWith effectsWith = race.getEffectsWith();
        for(Map.Entry<String, Integer> i : effectsWith.getGlobal().entrySet()) {
            PotionEffect effect;
            try {
                String[] parts = i.getKey().split(":", 2);
                if (parts.length != 2)
                    throw new IllegalArgumentException("Invalid NamespacedKey format");

                PotionEffectType potionEffectType = Registry.POTION_EFFECT_TYPE.get(new NamespacedKey(parts[0], parts[1]));
                if (potionEffectType == null)
                    throw new NullPointerException();

                effect = new PotionEffect(potionEffectType, Integer.MAX_VALUE, i.getValue());
            } catch (Exception exception) {
                ERaces.getInstance().getLogger().warning("Произошла ошибка при парсинге эффекта с именем: "+i.getKey()+
                        ", и силой: "+ i.getValue()+ ", ошибка:"+exception.getMessage());
                continue;
            }

            player.addPotionEffect(effect);
        }
    }
}
