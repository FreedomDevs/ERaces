package dev.elysium.eraces.abilities.abils.buffs.mana

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.AbilityUtils
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import dev.elysium.eraces.listeners.custom.ManaConsumeEvent
import dev.elysium.eraces.listeners.custom.ManaRegenerationEvent
import dev.elysium.eraces.utils.actionMsg
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffectType
import java.util.UUID
import kotlin.math.cos
import kotlin.math.sin

@RegisterAbility
@Suppress("unused")
class OldAcquaintancesAbility : BaseEffectsAbility(
    id = "oldacquaintances", defaultCooldown = "5m", defaultEffects = linkedMapOf(
        "regeneration" to EffectData("10s", 2, PotionEffectType.REGENERATION)
    )
), Listener {
    private val manaRegenBoost = mutableSetOf<UUID>()
    private val manaConsumeBoost = mutableSetOf<UUID>()

    private var manaRegen: Double = 0.5 // +50% regeneration mana
    private var manaRegenTime: String = "10s"

    private var manaConsume: Double = 0.2 // -20% consume mana
    private var manaConsumeTime: String = "2m"

    override fun customActivate(player: Player) {
        val plugin = ERaces.getInstance()

        if (manaRegenBoost.contains(player.uniqueId) || manaConsumeBoost.contains(player.uniqueId)) {
            player.actionMsg( "<red>Ты уже используешь способность 'Старые знакомства'!")
            return
        }

        manaRegenBoost.add(player.uniqueId)
        AbilityUtils.runLater(plugin, manaRegenTime) {
            manaRegenBoost.remove(player.uniqueId)
        }

        manaConsumeBoost.add(player.uniqueId)
        AbilityUtils.runLater(plugin, manaConsumeTime) {
            manaConsumeBoost.remove(player.uniqueId)
        }

        spawnParticle(plugin, player)
    }

    private fun spawnParticle(plugin: ERaces, player: Player) {
        AbilityUtils.runRepeatingForDuration(plugin, manaRegenTime, 2L) { tick ->
            if (!manaRegenBoost.contains(player.uniqueId)) return@runRepeatingForDuration

            val world = player.world
            val center = player.location.clone().add(0.0, 1.2, 0.0)

            val angle = tick * 0.3
            val radius = 0.8

            val x = radius * cos(angle)
            val z = radius * sin(angle)
            val loc = center.clone().add(x, 0.0, z)

            world.spawnParticle(
                Particle.DUST,
                loc, 1, 0.0, 0.0, 0.0,
                Particle.DustOptions(Color.fromRGB(85, 170, 255), 1.6f)
            )
        }

        AbilityUtils.runRepeatingForDuration(plugin, manaConsumeTime, 2L) { tick ->
            if (!manaConsumeBoost.contains(player.uniqueId)) return@runRepeatingForDuration

            val world = player.world
            val center = player.location.clone().add(0.0, 1.2, 0.0)

            val angle = -tick * 0.25
            val radius = 1.0

            val x = radius * cos(angle)
            val z = radius * sin(angle)
            val loc = center.clone().add(x, 0.0, z)

            world.spawnParticle(
                Particle.DUST,
                loc, 1, 0.0, 0.0, 0.0,
                Particle.DustOptions(Color.fromRGB(255, 140, 0), 1.6f)
            )
        }
    }

    @EventHandler
    fun onManaRegenerated(event: ManaRegenerationEvent) {
        val player = event.player
        if (manaRegenBoost.contains(player.uniqueId)) {
            event.regenAmount = event.regenAmount * (1.0 + manaRegen)

        }

    }

    @EventHandler
    fun onManaConsume(event: ManaConsumeEvent) {
        val player = event.player
        if (manaConsumeBoost.contains(player.uniqueId)) {
            event.amount = event.amount * (1.0 - manaConsume)
        }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        super.loadCustomParams(cfg)
        ConfigHelper.with(cfg) {
            read("mana_regen", ::manaRegen)
            read("mana_regen_time", ::manaRegenTime)
            read("mana_consume", ::manaConsume)
            read("mana_consume_time", ::manaConsumeTime)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        super.writeCustomDefaults(cfg)
        ConfigHelper.with(cfg) {
            write("mana_regen", manaRegen)
            write("mana_regen_time", manaRegenTime)
            write("mana_consume", manaConsume)
            write("mana_consume_time", manaConsumeTime)
        }
    }
}