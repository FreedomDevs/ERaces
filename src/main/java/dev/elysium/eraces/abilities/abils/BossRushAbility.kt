package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.IAbility
import dev.elysium.eraces.abilities.abils.RageModeAbility.EffectData
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.time.toDuration

class BossRushAbility : IAbility {
    override val id: String = "bossrush"
    private var cooldown: Int = 60

    private var healing: EffectData = EffectData(60, 3)
    private var resistant: EffectData = EffectData(120, 3)
    private var power = EffectData(120, 3)
    private var regeneration = EffectData(120, 2)
    private var absorption = EffectData(300, 2)
    private var speed = EffectData(120, 2)
    private var nightvision = EffectData(1200, 255)

    override fun activate(player: Player) {
        val effects = listOf(
            PotionEffect(
                PotionEffectType.HEALTH_BOOST,
                healing.duration,
                healing.level,
                false,
                true
            ),
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
            ),
            PotionEffect(
                PotionEffectType.SPEED,
                speed.duration,
                speed.duration,
                false,
                true
            ),
            PotionEffect(
                PotionEffectType.NIGHT_VISION,
                nightvision.duration,
                nightvision.level,
                false,
                true
            )
        )
        player.addPotionEffects(effects)
    }

    override fun loadParams(cfg: YamlConfiguration) {
        cooldown = cfg.getInt("cooldown", 60)
        healing.duration = cfg.getInt("healihg_duration", 60)
        healing.level = cfg.getInt("healing_level", 3)

        resistant.duration = cfg.getInt("resistant_duration", 120)
        resistant.level = cfg.getInt("resistant_level", 3)

        power.duration = cfg.getInt("power_duration", 120)
        power.level = cfg.getInt("power_level", 3)

        regeneration.duration = cfg.getInt("regeneration_duration", 120)
        regeneration.level = cfg.getInt("regeneration_level", 2)

        speed.duration = cfg.getInt("speed_duration", 120)
        speed.level = cfg.getInt("speed_level")

        absorption.duration = cfg.getInt("absorption_duration", 300)
        absorption.level = cfg.getInt("absorption_level", 2)

        nightvision.duration = cfg.getInt("nightvision_duration", 1200)
        nightvision.level = cfg.getInt("nightvision_level", 255)

    }

    override fun writeDefaultParams(cfg: YamlConfiguration) {
        cfg.set("cooldown", 30)

        cfg.set("healing_duration", 60)
        cfg.set("healing_level", 3)

        cfg.set("resistant_duration", 120)
        cfg.set("resistant_level", 3)

        cfg.set("power_duration", 120)
        cfg.set("power_level", 3)

        cfg.set("regeneration_duration", 120)
        cfg.set("regeneration_level", 2)

        cfg.set("absorption_duration", 300)
        cfg.set("absorption_level", 2)

        cfg.set("nightvision_duration", 1200)
        cfg.set("nightvision_level", 255)

    }

    data class EffectData(
        var duration: Int,
        var level: Int)


}