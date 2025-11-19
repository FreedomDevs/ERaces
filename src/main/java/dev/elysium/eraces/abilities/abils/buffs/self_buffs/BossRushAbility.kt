package dev.elysium.eraces.abilities.abils.buffs.self_buffs

import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import org.bukkit.potion.PotionEffectType

@RegisterAbility
@Suppress("unused")
class BossRushAbility : BaseEffectsAbility(
    id = "bossrush",
    defaultCooldown = "1h",
    defaultEffects = linkedMapOf(
        "healing" to EffectData("60s", 3, PotionEffectType.HEALTH_BOOST),
        "resistant" to EffectData("2m", 3, PotionEffectType.RESISTANCE),
        "power" to EffectData("2m", 3, PotionEffectType.STRENGTH),
        "regeneration" to EffectData("2m", 2, PotionEffectType.REGENERATION),
        "absorption" to EffectData("5m", 2, PotionEffectType.ABSORPTION),
        "speed" to EffectData("2m", 2, PotionEffectType.SPEED),
        "nightvision" to EffectData("20m", 255, PotionEffectType.NIGHT_VISION)
    )
)