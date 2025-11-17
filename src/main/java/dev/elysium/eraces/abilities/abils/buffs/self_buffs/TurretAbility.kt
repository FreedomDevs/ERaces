package dev.elysium.eraces.abilities.abils.buffs.self_buffs

import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import org.bukkit.potion.PotionEffectType

class TurretAbility : BaseEffectsAbility(
 id = "turret",
    defaultCooldown = "5m",
    defaultEffects = linkedMapOf(
        "health_boost" to EffectData("60s", 5, PotionEffectType.HEALTH_BOOST),
        "slowness" to EffectData("60s", 1, PotionEffectType.SLOWNESS),
        "nausea" to EffectData("60s", 1, PotionEffectType.NAUSEA),
        "darkness" to EffectData("60s", 255, PotionEffectType.DARKNESS),
        )
)