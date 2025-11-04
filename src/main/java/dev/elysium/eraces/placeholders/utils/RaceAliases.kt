package dev.elysium.eraces.placeholders.utils

object RaceAliases {
    val map = mapOf(
        // Основные
        "hp" to "maxHp",
        "maxhp" to "maxHp",
        "health" to "maxHp",
        "armor" to "additionalArmor",
        "defense" to "additionalArmor",
        "regen" to "regenerationPerSec",
        "regeneration" to "regenerationPerSec",
        "shield" to "shieldUsage",
        "shieldusage" to "shieldUsage",

        // Акб
        "antikb" to "antiKnockbackLevel",
        "antiknockback" to "antiKnockbackLevel",
        "antikb_iron" to "antiKnocbackLevelWithIronArmorAndMore",
        "antiknockback_iron" to "antiKnocbackLevelWithIronArmorAndMore",

        // Выносливость
        "hunger" to "hungerLossMultiplier",
        "hungerloss" to "hungerLossMultiplier",
        "exhaustion" to "exhaustionMultiplier",

        // Дистанция удара
        "handdistance" to "handDistanceBonus",
        "reach" to "handDistanceBonus",
        "range" to "handDistanceBonus",

        // Мобы
        "peacefulafraid" to "peacefulMobsAfraid",
        "afraidmobs" to "peacefulMobsAfraid",
        "afraidexceptions" to "afraidMobsExceptions",
        "neutralmobs" to "neutralMobs",

        // Еда
        "forbiddenfood" to "forbiddenFoods",
        "forbiddenfoods" to "forbiddenFoods",
        "foodban" to "forbiddenFoods",

        // Визуальные эффекты
        "visual" to "visuals",
        "visualslist" to "visuals",

        // Подгруппы
        "weapon" to "weaponProficiency",
        "weapons" to "weaponProficiency",
        "gui" to "raceGuiConfig",
        "guiconfig" to "raceGuiConfig",
        "effects" to "effectsWith",
        "effectsw" to "effectsWith",
        "targeteffects" to "effectsTargeting",
        "targeting" to "effectsTargeting",
        "mana" to "manaRegenModifiers",
        "manaregen" to "manaRegenModifiers",
        "regenmodifiers" to "manaRegenModifiers",
        "clumsy" to "clumsinessChance",
        "clumsiness" to "clumsinessChance",
        "resurrection" to "chanceResurrection",
        "secondlife" to "chanceResurrection",
        "abilitieslist" to "abilities",
        "skills" to "abilities",
        "ability" to "abilities",

        // Прочее
        "missing" to "missingChance",
        "miss" to "missingChance",
        "firebonus" to "additionalFireDamage",
        "firedamage" to "additionalFireDamage",
        "falldmg" to "fallDamageMultiplier",
        "falldamage" to "fallDamageMultiplier",
        "secondlifecooldown" to "secondLifeCooldown",
        "secondlife_cd" to "secondLifeCooldown",
        "oxygen" to "oxygenBonus",
        "breath" to "oxygenBonus",
        "exclude" to "excludeFromRandom",
        "randomexclude" to "excludeFromRandom"
    )
}