package dev.elysium.eraces.abilities.abils.movement.jumps

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

@RegisterAbility
@Suppress("unused")
class DiveAbility : Listener, BaseCooldownAbility(id = "dive", defaultCooldown = "10m") {
    val key: NamespacedKey = NamespacedKey("eraces", "dive_ability")
    private var gravityMultiplier = -0.3

    @EventHandler
    @Suppress("Deprecation")
    fun onMove(event: PlayerMoveEvent) {
        // Вроде как опасно для безопасности
        if (event.player.isOnGround) {
            val attr = event.player.getAttribute(Attribute.GRAVITY) ?: return
            attr.removeModifier(key)
        }
    }

    override fun onActivate(player: Player) {
        val attr = player.getAttribute(Attribute.GRAVITY) ?: return
        attr.addModifier(AttributeModifier(key, gravityMultiplier, AttributeModifier.Operation.ADD_SCALAR))
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("gravity_multiplier", ::gravityMultiplier)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("gravity_multiplier", gravityMultiplier)
        }
    }
}