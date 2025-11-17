package dev.elysium.eraces.abilities.abils.buffs.self_buffs

import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import org.bukkit.potion.PotionEffectType

class BlockAbility : BaseEffectsAbility(
    id = "block",
    defaultCooldown = "5m",
    defaultEffects = mapOf(
        "resistance" to EffectData("10s", 3, PotionEffectType.RESISTANCE)
    )
)