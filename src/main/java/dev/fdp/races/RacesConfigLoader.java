package dev.fdp.races;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class RacesConfigLoader {
  public static void checkConfigExists(JavaPlugin plugin) {
    // Убедимся, что папка данных существует
    File dataFolder = plugin.getDataFolder();
    if (!dataFolder.exists()) {
      dataFolder.mkdirs();
    }

    // Определяем файл конфигурации
    File racesFile = new File(dataFolder, "races.yml");

    // Если файла нет, копируем его из ресурсов (изнутри jar)
    if (!racesFile.exists()) {
      plugin.saveResource("races.yml", false);
    }
  }

  public static Map<String, Race> loadConfig(JavaPlugin plugin) {
    Map<String, Race> races = new HashMap<>();

    File file = new File(plugin.getDataFolder(), "races.yml");

    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    for (String key : config.getKeys(false)) {
      ConfigurationSection section = config.getConfigurationSection(key);
      if (section == null)
        continue; // на случай пустой секции

      Race race = new Race();
      race.setId(key);
      race.setName(section.getString("name", key));
      race.setMaxHp(section.getDouble("max_hp", 20.0));
      race.setHungerLossMultiplier(section.getDouble("hunger_loss_multiplier", 1.0));
      race.setMineSpeed(section.getDouble("mine_speed", 1.0));
      race.setHandDistanceBonus(section.getInt("hand_distance-bonus", 0));
      race.setAdditionalArmor(section.getDouble("additional_armor", 0.0));
      race.setShieldUsage(section.getBoolean("shield_usage", true));
      race.setRegenerationPerSec(section.getDouble("regeneration_per_sec", 0.0));
      if (section.isList("forbidden_foods")) {
        race.setForbiddenFoods(section.getStringList("forbidden_foods"));
      }

      // Загружаем вложенную секцию weapon_proficiency, если есть
      if (section.isConfigurationSection("weapon_proficiency")) {
        ConfigurationSection wpSection = section.getConfigurationSection("weapon_proficiency");
        WeaponProficiency wp = new WeaponProficiency();
        wp.setBowDamageMultiplier(wpSection.getDouble("bow_damage_multiplier", 1.0));
        wp.setBowProjectileSpeedMultiplier(wpSection.getDouble("bow_projectile_speed_multiplier", 1.0));
        wp.setSwordDamageMultiplier(wpSection.getDouble("sword_damage_multiplier", 1.0));
        wp.setAxeDamageMultiplier(wpSection.getDouble("axe_damage_multiplier", 1.0));
        wp.setMaceDamageMultiplier(wpSection.getDouble("mace_damage_multiplier", 1.0));
        wp.setHandDamageAdditional(wpSection.getDouble("hand_damage_additional", 0.0));
        race.setWeaponProficiency(wp);
      }

      races.put(key, race);
    }
    return races;
  }
}
