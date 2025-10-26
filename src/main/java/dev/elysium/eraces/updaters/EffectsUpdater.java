package dev.elysium.eraces.updaters;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.datatypes.*;
import dev.elysium.eraces.updaters.base.IUpdater;
import dev.elysium.eraces.utils.EffectUtils;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class EffectsUpdater implements IUpdater {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int biomeTask;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int lightTask;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int blockTask;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int timeTask;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int resurrectionTask;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final int waterTask;

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

        timeTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                ERaces.getInstance(),
                this::applyTimeEffects,
                0,
                40
        );

        resurrectionTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                ERaces.getInstance(),
                this::applyResurrectionEffects,
                0,
                20
        );

        waterTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                ERaces.getInstance(),
                this::applyWaterEffects,
                0,
                20
        );
    }

    private void applyWaterEffects() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Race race = ERaces.getInstance().getContext().playerDataManager.getPlayerRace(player);
            Block block = player.getLocation().getBlock();
            World world = player.getLocation().getWorld();
            boolean isHighest = player.getLocation().getY() >= world.getHighestBlockYAt(player.getLocation());

            if (block.getType() == Material.WATER || (isHighest && (world.isThundering() || world.hasStorm()))) {
                EffectUtils.applyEffects(player, race.getEffectsWith().getInWater(), 23);
            }
        }
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
            Block block = player.getLocation().add(0, -1, 0).getBlock();

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

    private void applyResurrectionEffects() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Race race = ERaces.getInstance().getContext().playerDataManager.getPlayerRace(player);
            if (race == null) continue;

            NamespacedKey key = new NamespacedKey(ERaces.getInstance(), "resurrections_done");
            Integer resurrectionsDone = player.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);

            if (resurrectionsDone == null || resurrectionsDone <= 0) {
                Map<String, Integer> effectsList = race.getEffectsWith().getEffectsWithResurrection();
                if (effectsList == null || effectsList.isEmpty()) continue;

                Map<String, Integer> safeEffects =  effectsList.entrySet().stream()
                        .filter(e -> e.getKey() != null && e.getValue() != null)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                if (!safeEffects.isEmpty()) {
                    EffectUtils.applyEffects(player, safeEffects, 25);
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
