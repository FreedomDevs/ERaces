package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.IAbility
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class RageModeAbility : IAbility {
    override val id: String = "ragemode"
    private var cooldown: Int = 30

    private var resistant: EffectData = EffectData(60, 3)
    private var power = EffectData(120, 3)
    private var regeneration = EffectData(60, 2)
    private var absorption = EffectData(300, 1)

    override fun activate(player: Player) {
        val effects = listOf(
            PotionEffect(
                PotionEffectType.RESISTANCE,
                resistant.duration,
                resistant.level,
                false,
                true
            ),
            PotionEffect(
                PotionEffectType.STRENGTH,
                power.duration,
                power.level,
                false,
                true
            ),
            PotionEffect(
                PotionEffectType.REGENERATION,
                regeneration.duration,
                regeneration.level,
                false,
                true
            ),
            PotionEffect(
                PotionEffectType.ABSORPTION,
                absorption.duration,
                absorption.level,
                false,
                true
            )
        )
        player.addPotionEffects(effects)
    }

    override fun loadParams(cfg: YamlConfiguration) {
        cooldown = cfg.getInt("cooldown", 30)
        resistant.duration = cfg.getInt("resistant_duration", 60)
        resistant.level = cfg.getInt("resistant_level", 3)

        power.duration = cfg.getInt("power_duration", 120)
        power.level = cfg.getInt("power_level", 3)

        regeneration.duration = cfg.getInt("regeneration_duration", 60)
        regeneration.level = cfg.getInt("regeneration_level", 2)

        absorption.duration = cfg.getInt("absorption_duration", 300)
        absorption.level = cfg.getInt("absorption_level", 1)
    }

    override fun writeDefaultParams(cfg: YamlConfiguration) {
        cfg.set("cooldown", 30)

        cfg.set("resistant_duration", 60)
        cfg.set("resistant_level", 3)

        cfg.set("power_duration", 120)
        cfg.set("power_level", 3)

        cfg.set("regeneration_duration", 60)
        cfg.set("regeneration_level", 20)

        cfg.set("absorption_duration", 300)
        cfg.set("absorption_level", 1)
    }

    data class EffectData(
        var duration: Int,
        var level: Int
    )
}