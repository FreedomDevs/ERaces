package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import org.bukkit.potion.PotionEffectType

class ForestSpiritAbility : BaseEffectsAbility(
    id = "forestspirit",
    defaultCooldown = "10m",
    defaultEffects = linkedMapOf(
        "regeneration" to EffectData("2m", 2, PotionEffectType.REGENERATION),
        "nightvision" to EffectData("20m", 255, PotionEffectType.NIGHT_VISION),
        "resistant" to EffectData("400s", 1, PotionEffectType.RESISTANCE)
    )
)
