package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import org.bukkit.potion.PotionEffectType

class SharpClawsAbility: BaseEffectsAbility(id = "sharpclaws", defaultEffects = linkedMapOf(
    "strength" to EffectData("3m", 2, PotionEffectType.STRENGTH)
), defaultCooldown = "10m")