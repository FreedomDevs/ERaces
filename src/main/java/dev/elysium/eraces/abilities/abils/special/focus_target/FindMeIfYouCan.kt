package dev.elysium.eraces.abilities.abils.special.focus_target

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.TimeParser
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.UUID

@RegisterAbility
@Suppress("unused")
class FindMeIfYouCan : Listener, BaseCooldownAbility(id = "find_me_if_you_can", defaultCooldown = "3m") {
    private var duration: String = "20s"

    private val playersOnEffect: MutableMap<UUID, Long> = mutableMapOf()

    @EventHandler
    fun onPlayerDamageEvent(event: EntityDamageEvent) {
        val player = when {
            event.entity is Player -> event.entity
            event.damageSource.causingEntity is Player -> event.damageSource.causingEntity
            else -> null
        } as? Player ?: return

        val value = playersOnEffect[player.uniqueId] ?: return

        if (value + TimeParser.parseToMilliseconds(duration) < System.currentTimeMillis()) {
            playersOnEffect.remove(player.uniqueId)
            return
        }

        val effect = player.getPotionEffect(PotionEffectType.INVISIBILITY)
        if (effect != null && effect.duration < TimeParser.parseToTicks(duration)) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY)
        }
    }

    override fun onActivate(player: Player) {
        player.addPotionEffect(
            PotionEffect(
                PotionEffectType.INVISIBILITY,
                TimeParser.parseToTicks(duration).toInt(),
                0,
                false
            )
        )
        playersOnEffect[player.uniqueId] = System.currentTimeMillis()
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("duration", ::duration)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("duration", duration)
        }
    }
}