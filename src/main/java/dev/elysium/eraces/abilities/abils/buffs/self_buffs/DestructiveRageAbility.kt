package dev.elysium.eraces.abilities.abils.buffs.self_buffs

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.AbilityUtils
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
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

@RegisterAbility
@Suppress("unused")
class DestructiveRageAbility : BaseEffectsAbility(
    "destructiverage", "70s",
    defaultEffects = mapOf(
        "speed" to EffectData("20s", 1, PotionEffectType.SPEED),
        "jump_boost" to EffectData("20s", 1, PotionEffectType.JUMP_BOOST),
    )), Listener {

    var additionalArmor = 3.0
    var armorDuration = "20s"

    override fun customActivate(player: Player) {
        addArmor(player)

        // убираем броню через 20 секунд
        AbilityUtils.runLater(ERaces.getInstance(), armorDuration) {
            removeArmor(player)
        }
    }

    // нужно убрать бонусную броню перед выходом игрока
    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        removeArmor(event.player);
    }

    val addArmorKey: NamespacedKey = NamespacedKey("eraces", "destructive_rage_ability_armor_bonus")

    private fun addArmor(player: Player) {
        val attribute = player.getAttribute(Attribute.ARMOR) ?: return
        attribute.addModifier(AttributeModifier(addArmorKey, additionalArmor, AttributeModifier.Operation.ADD_NUMBER))
    }

    private fun removeArmor(player: Player) {
        val attribute = player.getAttribute(Attribute.ARMOR) ?: return
        attribute.removeModifier(addArmorKey)
    }


    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("additionalArmor", ::additionalArmor)
            read("armorDuration", ::armorDuration)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("additionalArmor", additionalArmor)
            write("armorDuraton", armorDuration)
        }
    }
}