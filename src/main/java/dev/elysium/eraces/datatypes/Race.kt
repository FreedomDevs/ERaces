package dev.elysium.eraces.datatypes

class Race {
    var id: String? = null

    @RaceProperty(path = "max_hp", type = FieldType.DOUBLE)
    var maxHp: Double = 20.0

    @RaceProperty(path = "hunger_loss_multiplier", type = FieldType.DOUBLE)
    var hungerLossMultiplier: Double = 1.0  //- хз ни где не юзается но вроде и небыло упоминания что он вырезан поэтому оставлю от греха подальше

    @RaceProperty(path = "hand_distance_bonus", type = FieldType.INT)
    var handDistanceBonus: Int = 0

    @RaceProperty(path = "additional_armor", type = FieldType.DOUBLE)
    var additionalArmor: Double = 0.0

    @RaceProperty(path = "shield_usage", type = FieldType.BOOLEAN)
    var shieldUsage: Boolean = true

    @RaceProperty(path = "regeneration_per_sec", type = FieldType.DOUBLE)
    var regenerationPerSec: Double = 0.0

    @RaceProperty(path = "antiknockback_level", type = FieldType.INT)
    var antiKnockbackLevel: Int = 0

    @RaceProperty(path = "antiknockback_level_with_iron_and_more_armor", type = FieldType.INT)
    var antiKnocbackLevelWithIronArmorAndMore: Int = 0

    @RaceProperty(path = "exclude_from_random", type = FieldType.BOOLEAN)
    @get:JvmName("isExcludeFromRandom")
    var excludeFromRandom: Boolean = false

    @RaceProperty(path = "peaceful_mobs_afraid", type = FieldType.BOOLEAN)
    var peacefulMobsAfraid: Boolean = false

    @RaceProperty(path = "forbidden_foods", type = FieldType.LIST)
    var forbiddenFoods: MutableList<String> = mutableListOf()

    @RaceProperty(path = "visuals", type = FieldType.LIST)
    var visuals: MutableList<String> = mutableListOf()

    @RaceProperty(path = "afraid_mobs_exceptions", type = FieldType.LIST)
    var afraidMobsExceptions: MutableList<String> = mutableListOf()

    @RaceProperty(path = "exhaustion_multiplier", type = FieldType.DOUBLE)
    var exhaustionMultiplier: Double = 1.0

    @RaceProperty(path = "slowness_with_iron_and_more_armor", type = FieldType.INT)
    var slownessWithIronAndMoreArmor: Int = 0

    @RaceProperty(path = "weapon_proficiency", type = FieldType.SUBGROUP)
    var weaponProficiency: WeaponProficiency = WeaponProficiency()

    @RaceProperty(path = "gui_config", type = FieldType.SUBGROUP)
    var raceGuiConfig: RaceGuiConfig = RaceGuiConfig()

    @RaceProperty(path = "effects_with", type = FieldType.SUBGROUP)
    var effectsWith: EffectsWith = EffectsWith()

    @RaceProperty(path = "neutral_mobs", type = FieldType.LIST)
    var neutralMobs: MutableList<String> = mutableListOf()

    @RaceProperty(path = "abilities", type = FieldType.LIST)
    var abilities: MutableList<String> = mutableListOf()

    @RaceProperty(path = "mana_regeneration_modifiers", type = FieldType.LIST_SUBGROUP)
    var manaRegenModifiers:  MutableList<ConditionalModifier> = mutableListOf()

    @RaceProperty(path = "clumsiness_chance", type = FieldType.SUBGROUP)
    var clumsinessChance: ClumsinessChance = ClumsinessChance()

    @RaceProperty(path = "chance_resurrection", type = FieldType.SUBGROUP)
    var chanceResurrection: ChanceResurrection = ChanceResurrection()

    @RaceProperty(path = "effects_targeting", type = FieldType.SUBGROUP)
    var effectsTargeting: EffectTargeting = EffectTargeting()

    @RaceProperty(path = "missing_chance", type = FieldType.DOUBLE)
    var missingChance: Double = 0.0

    @RaceProperty(path = "additional_fire_damage", type = FieldType.DOUBLE)
    var additionalFireDamage: Double = 0.0

    @RaceProperty(path = "fall_damage_multiplier", type = FieldType.DOUBLE)
    var fallDamageMultiplier: Double = 1.0

    @RaceProperty(path = "second_life_cooldown", type = FieldType.STRING)
    var secondLifeCooldown: String = ""

    @RaceProperty(path = "oxygen_bonus", type = FieldType.DOUBLE)
    var oxygenBonus: Double = 0.0

    @RaceProperty(path = "attack_speed_multiplier", type = FieldType.DOUBLE)
    var attackSpeedMultiplier: Double = 1.0

    @RaceProperty(path = "move_speed_multiplier", type = FieldType.DOUBLE)
    var moveSpeedMultiplier: Double = 1.0

    @RaceProperty(path = "base_damage_bonus", type = FieldType.DOUBLE)
    var baseDamageBonus: Double = 0.0

    @RaceProperty(path = "additional_scale", type = FieldType.DOUBLE)
    var additionalScale: Double = 0.0
}
