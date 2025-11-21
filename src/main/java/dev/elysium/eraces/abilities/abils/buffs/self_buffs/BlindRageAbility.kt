package dev.elysium.eraces.abilities.abils.buffs.self_buffs

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.AbilityUtils
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import org.bukkit.attribute.Attribute
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.potion.PotionEffectType

class BlindRageAbility : BaseEffectsAbility(id = "blindrage", defaultCooldown = "65s",
    defaultEffects = mapOf(
        "blindness" to EffectData("15s", 1, PotionEffectType.BLINDNESS)
    )), Listener {

    var armorDuration = "15s"
    var additionalArmor = 4.0

    override fun customActivate(player: Player) {
        addArmor(player);

        AbilityUtils.runLater(ERaces.getInstance(), armorDuration) {
            removeArmor(player)
        }
    }

    // нужно убрать бонусную броню перед выходом игрока
    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        removeArmor(event.player);
    }

    private fun addArmor(player: Player) {
        val attribute = player.getAttribute(Attribute.ARMOR)
        if (attribute != null)
            attribute.setBaseValue(attribute.baseValue + additionalArmor)
    }

    private fun removeArmor(player: Player) {
        val attribute = player.getAttribute(Attribute.ARMOR)
        if (attribute != null)
            attribute.setBaseValue(attribute.baseValue - additionalArmor)
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("armorDuration", armorDuration)
            write("additionalArmor", additionalArmor)
        }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("armorDuration", ::armorDuration)
            read("additionalArmor", ::additionalArmor)
        }
    }
}