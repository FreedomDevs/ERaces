package dev.elysium.eraces.abilities.abils.buffs.self_buffs

import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import org.bukkit.potion.PotionEffectType

class RageModeAbility : BaseEffectsAbility(
    id = "ragemode",
    defaultCooldown = "30m",
    defaultEffects = linkedMapOf(
        "resistant" to EffectData("1m", 3, PotionEffectType.RESISTANCE),
        "power" to EffectData("2m", 3, PotionEffectType.STRENGTH),
        "regeneration" to EffectData("1m", 2, PotionEffectType.REGENERATION),
        "absorption" to EffectData("5m", 1, PotionEffectType.ABSORPTION)
    )
)