package dev.elysium.eraces.abilities.abils.buffs.self_buffs

import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import org.bukkit.potion.PotionEffectType

@RegisterAbility
@Suppress("unused")
class SharpClawsAbility: BaseEffectsAbility(id = "sharpclaws", defaultEffects = linkedMapOf(
    "strength" to EffectData("3m", 2, PotionEffectType.STRENGTH)
), defaultCooldown = "10m")