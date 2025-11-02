package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.AbilityUtils
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import dev.elysium.eraces.listeners.custom.ManaConsumeEvent
import dev.elysium.eraces.listeners.custom.ManaRegenerationEvent
import dev.elysium.eraces.utils.ChatUtil
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.attribute.Attribute
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffectType
import java.util.UUID
import kotlin.math.cos
import kotlin.math.sin

class SkillMastersAbility : BaseEffectsAbility(
    id = "skillmasters",
    defaultCooldown = "5m",
    defaultEffects = linkedMapOf(
        "resistance" to EffectData("2m", 2, PotionEffectType.RESISTANCE)
    )
), Listener {

    private val active = mutableSetOf<UUID>()

    private var manaRegenBonus: Double = 0.1 // +10%
    private var manaConsumeReduction: Double = 0.1 // -10%
    private var attackSpeedBonus: Double = 0.2
    private var duration: String = "2m"

    override fun customActivate(player: Player) {
        val plugin = ERaces.getInstance()

        if (active.contains(player.uniqueId)) {
            ChatUtil.sendAction(player, "<red>Ты уже используешь способность 'Духовный щит'!")
            return
        }

        active.add(player.uniqueId)

        val attr = player.getAttribute(Attribute.ATTACK_SPEED)
        val base = attr?.baseValue ?: 4.0
        attr?.baseValue = base + attackSpeedBonus

        spawnStartEffect(player)

        AbilityUtils.runLater(plugin, duration) {
            active.remove(player.uniqueId)
            attr?.baseValue = base
            ChatUtil.sendAction(player, "<gray>Эффект способности 'Духовный щит' закончился.")
        }

        spawnParticle(plugin, player)
    }

    private fun spawnParticle(plugin: ERaces, player: Player) {
        AbilityUtils.runRepeatingForDuration(plugin, duration, 3L) { tick ->
            if (!active.contains(player.uniqueId)) return@runRepeatingForDuration

            val world = player.world
            val center = player.location.clone().add(0.0, 1.0, 0.0)

            val angle = tick * 0.25
            val radius = 0.9

            val x = radius * cos(angle)
            val z = radius * sin(angle)
            val loc = center.clone().add(x, 0.0, z)

            world.spawnParticle(
                Particle.DUST,
                loc, 1, 0.0, 0.0, 0.0,
                Particle.DustOptions(Color.fromRGB(90, 190, 255), 1.5f)
            )

            if ((tick % 8).toLong() == 0L)
                world.spawnParticle(Particle.END_ROD, center, 1, 0.3, 0.2, 0.3, 0.0)
        }
    }

    private fun spawnStartEffect(player: Player) {
        val world = player.world
        val loc = player.location.clone().add(0.0, 1.0, 0.0)
        world.spawnParticle(Particle.WITCH, loc, 40, 0.8, 0.8, 0.8, 0.05)
    }

    @EventHandler
    fun onManaRegen(event: ManaRegenerationEvent) {
        val player = event.player
        if (active.contains(player.uniqueId)) {
            event.regenAmount *= (1.0 + manaRegenBonus)
        }
    }

    @EventHandler
    fun onManaConsume(event: ManaConsumeEvent) {
        val player = event.player
        if (active.contains(player.uniqueId)) {
            event.amount *= (1.0 - manaConsumeReduction)
        }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        super.loadCustomParams(cfg)
        ConfigHelper.with(cfg) {
            read("mana_regen_bonus", ::manaRegenBonus)
            read("mana_consume_reduction", ::manaConsumeReduction)
            read("attack_speed_bonus", ::attackSpeedBonus)
            read("duration", ::duration)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        super.writeCustomDefaults(cfg)
        ConfigHelper.with(cfg) {
            write("mana_regen_bonus", manaRegenBonus)
            write("mana_consume_reduction", manaConsumeReduction)
            write("attack_speed_bonus", attackSpeedBonus)
            write("duration", duration)
        }
    }
}
