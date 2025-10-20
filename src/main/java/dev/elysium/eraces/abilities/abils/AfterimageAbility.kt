package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import org.bukkit.potion.PotionEffectType

class AfterimageAbility : BaseEffectsAbility(
    id = "afterimage", defaultCooldown = "10m",

    defaultEffects = linkedMapOf(
        "speed" to EffectData("2m", 2, PotionEffectType.SPEED),
        "strenght" to EffectData("20m", 255, PotionEffectType.STRENGTH),
        "nightvision" to EffectData("400s", 1, PotionEffectType.NIGHT_VISION),
        "invisibility" to EffectData( "30s", 255, PotionEffectType.INVISIBILITY)
    )
)