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

import java.util.Objects;

public class EffectsUpdater implements IUpdater {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int biomeTask;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int lightTask;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int blockTask;

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
    }

    private void applyBiomeEffects() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Race race = ERaces.getPlayerMng().getPlayerRace(player);
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
            Race race = ERaces.getPlayerMng().getPlayerRace(player);
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
            Race race = ERaces.getPlayerMng().getPlayerRace(player);
            Block block = player.getLocation().getBlock();

            for (EffectsWithBlock effectConfig : race.getEffectsWith().getEffectsWithBlocks()) {
                if (effectConfig.getBlocks().contains(block.getType().name())) {
                    EffectUtils.applyEffects(player, effectConfig.getEffects(), 20);
                }
            }
        }
    }

    @Override
    public void update(Race race, Player player) {
        EffectUtils.applyEffects(player, race.getEffectsWith().getGlobal(), Integer.MAX_VALUE);
    }
}
