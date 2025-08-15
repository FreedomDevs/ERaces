package dev.fdp.races.config;

import dev.fdp.races.Race;
import dev.fdp.races.RaceGuiConfig;
import dev.fdp.races.WeaponProficiency;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class RacesConfigManager {
    private static final String FILE_NAME = "races.yml";

    @Getter
    private Map<String, Race> races = null;

    private final YamlManager cfgManager;
    private final JavaPlugin plugin;

    public RacesConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.cfgManager = new YamlManager(this.plugin, FILE_NAME, true);
        reloadConfig();
    }


    public void reloadConfig() {
        races = loadConfig();
        plugin.getLogger().info("Загружено: " + races.size() + " рас");
    }

    private Map<String, Race> loadConfig() {
        Map<String, Race> races = new HashMap<>();
        YamlConfiguration config = cfgManager.getConfig();

        for (String key : config.getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection(key);
            if (section == null)
                continue;

            Race race = new Race();
            race.setId(key);

            if (!section.isConfigurationSection("gui_config")) {
                plugin.getLogger()
                        .warning("Race \"" + key + "\" does not have a gui_config section (loading failed)");
                continue;
            }

            ConfigurationSection guiConfigSection = section.getConfigurationSection("gui_config");
            RaceGuiConfig raceGuiConfig = new RaceGuiConfig();
            raceGuiConfig.setName(guiConfigSection.getString("name", "name_undefined"));

            race.setRaceGuiConfig(raceGuiConfig);

            race.setMaxHp(section.getDouble("max_hp", 20.0));
            race.setHungerLossMultiplier(section.getDouble("hunger_loss_multiplier", 1.0));
            race.setMineSpeed(section.getDouble("mine_speed", 1.0));
            race.setHandDistanceBonus(section.getInt("hand_distance-bonus", 0));
            race.setAdditionalArmor(section.getDouble("additional_armor", 0.0));
            race.setShieldUsage(section.getBoolean("shield_usage", true));
            race.setRegenerationPerSec(section.getDouble("regeneration_per_sec", 0.0));
            race.setRunningSpeed(section.getInt("running_speed", 0));
            race.setAntiKnockbackLevel(section.getInt("antiknockback_level", 0));
            race.setSlowdownLevel(section.getInt("slowdown_level", 0));
            race.setDamageResistanceLevel(section.getInt("damage_resistance_level", 0));
            race.setPeacefulMobsAfraid(section.getBoolean("peaceful_mobs_afraid", false));
            race.setAntiKnocbackLevelWithIronArmorAndMore(
                    section.getInt("knockback_resistance_level_with_iron_and_more_armor", 0));
            race.setExcludeFromRandom(section.getBoolean("exclude_from_random", false));

            if (section.isList("forbidden_foods")) {
                race.setForbiddenFoods(section.getStringList("forbidden_foods"));
            }

            if (section.isList("biome_speed")) {
                race.setBiomeSpeed(section.getStringList("biome_speed"));
            }

            WeaponProficiency wp = new WeaponProficiency();

            if (section.isConfigurationSection("weapon_proficiency")) {
                ConfigurationSection wpSection = section.getConfigurationSection("weapon_proficiency");
                wp.setBowDamageMultiplier(wpSection.getDouble("bow_damage_multiplier", wp.getBowDamageMultiplier()));
                wp.setBowProjectileSpeedMultiplier(
                        wpSection.getDouble("bow_projectile_speed_multiplier", wp.getBowProjectileSpeedMultiplier()));
                wp.setSwordDamageMultiplier(wpSection.getDouble("sword_damage_multiplier", wp.getSwordDamageMultiplier()));
                wp.setAxeDamageMultiplier(wpSection.getDouble("axe_damage_multiplier", wp.getAxeDamageMultiplier()));
                wp.setMaceDamageMultiplier(wpSection.getDouble("mace_damage_multiplier", wp.getMaceDamageMultiplier()));
                wp.setHandDamageAdditional(wpSection.getDouble("hand_damage_additional", wp.getHandDamageAdditional()));
                wp.setDamageAdditional(wpSection.getDouble("damage_additional", wp.getDamageAdditional()));
                wp.setDamageAdditionalWithIronAndLowerArmor(wpSection.getDouble("damage_additional_with_iron_and_lower_armor:",
                        wp.getDamageAdditionalWithIronAndLowerArmor()));
                wp.setDamageAdditionalWithWolfsNear(
                        wpSection.getDouble("damage_additional_with_wolfs_near", wp.getDamageAdditionalWithWolfsNear()));
                wp.setDualWeaponDamageAdditional(
                        wpSection.getDouble("dual_weapon_damage_additional", wp.getDualWeaponDamageAdditional()));

            }

            if (section.isList("visuals")) {
                race.setVisuals(section.getStringList("visuals"));
            }

            race.setWeaponProficiency(wp);
            races.put(key, race);
        }
        return races;
    }
}
