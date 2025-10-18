package dev.elysium.eraces.updaters;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.datatypes.*;
import dev.elysium.eraces.updaters.base.IUpdater;
import dev.elysium.eraces.utils.EffectUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Objects;

public class EffectsUpdater implements IUpdater {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int biomeTask;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int lightTask;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int blockTask;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int timeTask;

    public enum LightType {
        SUM, BLOCK, SKY;

        public static LightType fromString(String str) {
            try {
                return LightType.valueOf(str.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Неверный тип света: " + str, e);
            }
        }
    }

    public EffectsUpdater() {
        biomeTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                ERaces.getInstance(),
                this::applyBiomeEffects,
                0,
                20
        );

        lightTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                ERaces.getInstance(),
                this::applyLightEffects,
                0,
                5
        );

        blockTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                ERaces.getInstance(),
                this::applyBlockEffects,
                0,
                5
        );

        timeTask =  Bukkit.getScheduler().scheduleSyncRepeatingTask(
                ERaces.getInstance(),
                this::applyTimeEffects,
                0,
                40
        );
    }

    private void applyBiomeEffects() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Race race = ERaces.getInstance().getContext().playerDataManager.getPlayerRace(player);
            Biome currentBiome = player.getWorld().getBiome(player.getLocation());

            for (EffectsWithBiome effectConfig : race.getEffectsWith().getEffectsWithBiomes()) {
                if (effectConfig.getBiomes().stream().map(NamespacedKey::fromString)
                        .anyMatch(biomeKey -> Objects.equals(biomeKey, currentBiome.getKey()))) {
                    EffectUtils.applyEffects(player, effectConfig.getEffects(), 23);
                }
            }
        }
    }

    private void applyLightEffects() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Race race = ERaces.getInstance().getContext().playerDataManager.getPlayerRace(player);
            Block block = player.getLocation().getBlock();

            for (EffectsWithLight effectConfig : race.getEffectsWith().getEffectsWithLights()) {
                try {
                    if (EffectUtils.isLightLevelInRange(block, effectConfig.getLightType(), effectConfig.getMinLight(), effectConfig.getMaxLight())) {
                        EffectUtils.applyEffects(player, effectConfig.getEffects(), 7);
                    }
                } catch (IllegalArgumentException ex) {
                    ERaces.getInstance().getLogger().warning(ex.getMessage());
                }
            }
        }
    }

    private void applyBlockEffects() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Race race = ERaces.getInstance().getContext().playerDataManager.getPlayerRace(player);
            Block block = player.getLocation().getBlock();

            for (EffectsWithBlock effectConfig : race.getEffectsWith().getEffectsWithBlocks()) {
                if (effectConfig.getBlocks().contains(block.getType().name())) {
                    EffectUtils.applyEffects(player, effectConfig.getEffects(), 20);
                }
            }
        }
    }

    private void applyTimeEffects() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Race race = ERaces.getInstance().getContext().playerDataManager.getPlayerRace(player);
            long time = player.getWorld().getTime();

            for (EffectsWithTime effectsCfg : race.getEffectsWith().getEffectsWithTime()) {
                if (time >= effectsCfg.getFrom() && time <= effectsCfg.getTo()) {
                    EffectUtils.applyEffects(player, effectsCfg.getEffects(), 40);
                }
            }
        }
    }

    @Override
    public void update(Race race, Player player) {
        for (PotionEffect effect : player.getActivePotionEffects())
            if (effect.getDuration() >= 20 * 60 * 60 * 24 * 356) // Удаляем эффекты с длительностью больше года
                player.removePotionEffect(effect.getType());

        EffectUtils.applyEffects(player, race.getEffectsWith().getGlobal(), Integer.MAX_VALUE);
    }
}
