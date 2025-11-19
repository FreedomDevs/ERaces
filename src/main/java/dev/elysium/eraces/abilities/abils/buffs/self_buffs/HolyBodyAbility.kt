package dev.elysium.eraces.abilities.abils.buffs.self_buffs

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.TimeParser
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@RegisterAbility
@Suppress("unused")
class HolyBodyAbility : BaseCooldownAbility(
    id = "holybody",
    defaultCooldown = "2m"
) {
    private var regenerationMinLevel = 1
    private var regenerationMaxLevel = 3
    private var regenerationMinDuration = "5s"
    private var regenerationMaxDuration = "10s"

    private lateinit var regenerationLevelRange: IntRange
    private lateinit var regenerationDurationRange: IntRange

    override fun onActivate(player: Player) {
        player.foodLevel = 20
        player.saturation = 20f

        val effect = PotionEffect(
            PotionEffectType.REGENERATION,
            regenerationDurationRange.random(),
            regenerationLevelRange.random() - 1,
            false
        )

        player.addPotionEffect(effect)
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("regeneration_min_level", ::regenerationMinLevel)
            read("regeneration_max_level", ::regenerationMaxLevel)
            read("regeneration_min_duration", ::regenerationMinDuration)
            read("regeneration_max_duration", ::regenerationMaxDuration)
        }

        regenerationLevelRange = regenerationMinLevel..regenerationMaxLevel
        regenerationDurationRange =
            TimeParser.parseToTicks(regenerationMinDuration).toInt()..TimeParser.parseToTicks(regenerationMaxDuration)
                .toInt()
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {

    }
}