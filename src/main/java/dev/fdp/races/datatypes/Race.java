package dev.fdp.races.datatypes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
public class Race {
    String id;
    @RaceProperty(path = "max_hp", type = FieldType.DOUBLE)
    double maxHp = 20.0;
    @RaceProperty(path = "hunger_loss_multiplier", type = FieldType.DOUBLE)
    double hungerLossMultiplier = 1.0;
    @RaceProperty(path = "haste_level", type = FieldType.INT)
    int hasteLevel = 1;
    @RaceProperty(path = "hand_distance_bonus", type = FieldType.INT)
    int handDistanceBonus = 0;
    @RaceProperty(path = "additional_armor", type = FieldType.DOUBLE)
    double additionalArmor = 0.0;
    @RaceProperty(path = "shield_usage", type = FieldType.BOOLEAN)
    boolean shieldUsage = true;
    @RaceProperty(path = "regeneration_per_sec", type = FieldType.DOUBLE)
    double regenerationPerSec = 0.0;
    @RaceProperty(path = "movement_speed_level", type = FieldType.INT)
    int movementSpeedLevel = 0;
    @RaceProperty(path = "antiknockback_level", type = FieldType.INT)
    int antiKnockbackLevel = 0;
    @RaceProperty(path = "antiknockback_level_with_iron_and_more_armor", type = FieldType.INT)
    int antiKnocbackLevelWithIronArmorAndMore = 0;
    @RaceProperty(path = "damage_resistance_level", type = FieldType.INT)
    int damageResistanceLevel = 0;
    @RaceProperty(path = "exclude_from_random", type = FieldType.BOOLEAN)
    boolean excludeFromRandom = false;
    @RaceProperty(path = "peaceful_mobs_afraid", type = FieldType.BOOLEAN)
    boolean peacefulMobsAfraid = false;
    @RaceProperty(path = "forbidden_foods", type = FieldType.LIST)
    List<String> forbiddenFoods = new ArrayList<>();
    @RaceProperty(path = "biome_speed", type = FieldType.LIST)
    List<String> biomeSpeed = new ArrayList<>();
    @RaceProperty(path = "visuals", type = FieldType.LIST)
    List<String> visuals = new ArrayList<>();
    @RaceProperty(path = "afraid_mobs_exceptions", type = FieldType.LIST)
    List<String> afraidMobsExceptions = new ArrayList<>();
    @RaceProperty(path = "exhaustion_multiplier", type = FieldType.DOUBLE)
    double exhaustionMultiplier = 1;
    @RaceProperty(path = "slowness_with_iron_and_more_armor", type = FieldType.INT)
    int slownessWithIronAndMoreArmor = 0;

    @RaceProperty(path = "weapon_proficiency", type = FieldType.SUBGROUP)
    WeaponProficiency weaponProficiency = new WeaponProficiency();
    @RaceProperty(path = "gui_config", type = FieldType.SUBGROUP)
    RaceGuiConfig raceGuiConfig = new RaceGuiConfig();
}
