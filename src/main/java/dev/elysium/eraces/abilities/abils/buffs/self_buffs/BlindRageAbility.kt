package dev.elysium.eraces.abilities.abils.buffs.self_buffs

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.AbilityUtils
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
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
            if (player.isOnline)
                removeArmor(player)
        }
    }

    // нужно убрать бонусную броню перед выходом игрока
    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        removeArmor(event.player);
    }

    val addArmorKey: NamespacedKey = NamespacedKey("eraces", "blind_rage_ability_armor_bonus")

    private fun addArmor(player: Player) {
        val attribute = player.getAttribute(Attribute.ARMOR) ?: return
        attribute.addModifier(AttributeModifier(addArmorKey, additionalArmor, AttributeModifier.Operation.ADD_NUMBER))
    }

    private fun removeArmor(player: Player) {
        val attribute = player.getAttribute(Attribute.ARMOR) ?: return
        attribute.removeModifier(addArmorKey)
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