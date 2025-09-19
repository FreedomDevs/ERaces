package dev.elysium.eraces.updaters;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.datatypes.EffectsWith;
import dev.elysium.eraces.datatypes.EffectsWithBiome;
import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.updaters.base.IUnloadable;
import dev.elysium.eraces.updaters.base.IUpdater;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;

public class EffectsUpdater implements IUpdater {
    private Integer task;

    public EffectsUpdater() {
        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(ERaces.getInstance(), () -> {
            for(Player player : Bukkit.getOnlinePlayers()) {
                Race race = ERaces.getPlayerMng().getPlayerRace(player);

                List<EffectsWithBiome> effectsWithBiomes = race.getEffectsWith().getEffectsWithBiomes();
                Biome biome = player.getWorld().getBiome(player.getLocation());
                NamespacedKey biomeKey = biome.getKey();
                for (EffectsWithBiome i : effectsWithBiomes) {
                    List<String> biomesFromConfig = i.getBiomes();

                    for (String b : biomesFromConfig) {
                        NamespacedKey keyFromString = NamespacedKey.minecraft(b.toLowerCase());
                        if (biomeKey.equals(keyFromString)) {
                            for(Map.Entry<String, Integer> j : i.getEffects().entrySet()) {
                                PotionEffect effect;
                                try {
                                    NamespacedKey key = NamespacedKey.fromString(j.getKey());
                                    if (key == null)
                                        throw new NullPointerException("key is null");

                                    PotionEffectType potionEffectType = Registry.POTION_EFFECT_TYPE.get(key);
                                    if (potionEffectType == null)
                                        throw new NullPointerException();

                                    effect = new PotionEffect(potionEffectType, 25, j.getValue()-1);
                                } catch (Exception exception) {
                                    ERaces.getInstance().getLogger().warning("Произошла ошибка при парсинге эффекта с именем: "+j.getKey()+
                                            ", и силой: "+ j.getValue()+ ", ошибка:"+exception.getMessage());
                                    continue;
                                }

                                player.addPotionEffect(effect);
                            }
                            break;
                        }
                    }
                }
            }
        }, 0, 20);
    }

    @Override
    public void update(Race race, Player player) {
        EffectsWith effectsWith = race.getEffectsWith();
        for(Map.Entry<String, Integer> i : effectsWith.getGlobal().entrySet()) {
            PotionEffect effect;
            try {
                NamespacedKey key = NamespacedKey.fromString(i.getKey());
                if (key == null)
                    throw new NullPointerException("key is null");

                PotionEffectType potionEffectType = Registry.POTION_EFFECT_TYPE.get(key);
                if (potionEffectType == null)
                    throw new NullPointerException();

                effect = new PotionEffect(potionEffectType, Integer.MAX_VALUE, i.getValue()-1);
            } catch (Exception exception) {
                ERaces.getInstance().getLogger().warning("Произошла ошибка при парсинге эффекта с именем: "+i.getKey()+
                        ", и силой: "+ i.getValue()+ ", ошибка:"+exception.getMessage());
                continue;
            }

            player.addPotionEffect(effect);
        }
    }
}
