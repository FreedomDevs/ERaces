package dev.elysium.eraces.abilities.abils.buffs.self_buffs

import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import org.bukkit.potion.PotionEffectType

class ShadowStepAbility : BaseEffectsAbility(
    id = "shadowstep",
    defaultCooldown = "2m",
    defaultEffects = linkedMapOf(
        "invisibility" to EffectData("4s", 2, PotionEffectType.INVISIBILITY),
        "speed" to EffectData("4s", 2, PotionEffectType.SPEED),
        )
)