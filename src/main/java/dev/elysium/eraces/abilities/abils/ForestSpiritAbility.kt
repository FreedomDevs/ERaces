package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.IAbility
import dev.elysium.eraces.abilities.abils.BossRushAbility.EffectData
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ForestSpiritAbility : IAbility {
    override val id: String = "forestspirit"
    private var cooldown: Int = 10

    private var regeneration = EffectData(120, 2)
    private var nightvision = EffectData(1200, 255)
    private var resistant: EffectData = EffectData(400, 1)

    override fun activate(player: Player) {
        val effects = listOf(
            PotionEffect(
                PotionEffectType.REGENERATION,
                regeneration.duration,
                regeneration.level,
                false,
                true
            ),
            PotionEffect(
                PotionEffectType.NIGHT_VISION,
                nightvision.duration,
                nightvision.level,
                false,
                true
            ),
            PotionEffect(
                PotionEffectType.RESISTANCE,
                resistant.duration,
                resistant.level,
                false,
                true
            )
        )
        player.addPotionEffects(effects)
    }

    override fun loadParams(cfg: YamlConfiguration) {
        cooldown = cfg.getInt("cooldown", 10)

        regeneration.duration = cfg.getInt("regeneration.duration", 120)
        regeneration.level = cfg.getInt("regeneration.level", 2)

        nightvision.duration = cfg.getInt("nightvision_duration", 1200)
        nightvision.level = cfg.getInt("nightvision_level", 255)

        resistant.duration = cfg.getInt("resistant_duration", 400)
        resistant.level = cfg.getInt("resistant_level", 1)
    }

    override fun writeDefaultParams(cfg: YamlConfiguration) {
        cfg.set("cooldown", 10)

        cfg.set("regeneration_duration", 120)
        cfg.set("regeneration_level", 2)

        cfg.set("nightvision_duration", 1200)
        cfg.set("nightvision_level", 255)

        cfg.set("resistant_duration", 400)
        cfg.set("resistant_level", 1)
    }

    data class EffectData(
        var duration: Int,
        var level: Int
    )

}