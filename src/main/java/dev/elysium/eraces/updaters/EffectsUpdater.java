package dev.elysium.eraces.updaters;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.datatypes.EffectsWith;
import dev.elysium.eraces.datatypes.EffectsWithBiome;
import dev.elysium.eraces.datatypes.EffectsWithLight;
import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.updaters.base.IUpdater;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;

public class EffectsUpdater implements IUpdater {
    private Integer biomeTask;
    private Integer lightTask;

    public enum LightType {
        SUM("sum"),
        BLOCK("block"),
        SKY("sky");

        private final String name;

        LightType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        // Метод для безопасного преобразования строки в enum
        public static LightType fromString(String str) throws IllegalArgumentException{
            for (LightType type : LightType.values()) {
                if (type.name.equalsIgnoreCase(str)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Неверный тип света: " + str);
        }
    }

    private static boolean testLightLevel(Block block, String type, Integer min, Integer max) throws IllegalArgumentException {
        LightType lightType = LightType.fromString(type);
        byte lightLevel;

        if (lightType == LightType.SUM) {
            lightLevel = block.getLightLevel();
        } else if (lightType == LightType.SKY) {
            lightLevel = block.getLightFromSky();
        } else if (lightType == LightType.BLOCK) {
            lightLevel = block.getLightFromBlocks();
        } else {
            throw new IllegalArgumentException("Неизвестный тип света: " + lightType);
        }

        return min <= lightLevel && max >= lightLevel;
    }

    public EffectsUpdater() {
        this.biomeTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(ERaces.getInstance(), () -> {
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

                                    effect = new PotionEffect(potionEffectType, 23, j.getValue()-1);
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

        this.lightTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(ERaces.getInstance(), () -> {
            for(Player player : Bukkit.getOnlinePlayers()) {
                Race race = ERaces.getPlayerMng().getPlayerRace(player);

                List<EffectsWithLight> effectsWithLights = race.getEffectsWith().getEffectsWithLights();
                Block block = player.getLocation().getBlock();
                for (EffectsWithLight i : effectsWithLights) {
                    boolean isLightLevel;
                    try {
                        isLightLevel = testLightLevel(block, i.getLightType(), i.getMinLight(), i.getMaxLight());
                    } catch (IllegalArgumentException ex) {
                        ERaces.getInstance().getLogger().warning(ex.getMessage());
                        continue;
                    }

                    if (isLightLevel) {
                        for (Map.Entry<String, Integer> j : i.getEffects().entrySet()) {
                            PotionEffect effect;
                            try {
                                NamespacedKey key = NamespacedKey.fromString(j.getKey());
                                if (key == null)
                                    throw new NullPointerException("key is null");

                                PotionEffectType potionEffectType = Registry.POTION_EFFECT_TYPE.get(key);
                                if (potionEffectType == null)
                                    throw new NullPointerException();

                                effect = new PotionEffect(potionEffectType, 7, j.getValue() - 1);
                            } catch (Exception exception) {
                                ERaces.getInstance().getLogger().warning("Произошла ошибка при парсинге эффекта с именем: " + j.getKey() +
                                        ", и силой: " + j.getValue() + ", ошибка:" + exception.getMessage());
                                continue;
                            }

                            player.addPotionEffect(effect);
                        }
                    }
                }
            }
        }, 0, 5) ;

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
