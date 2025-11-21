package dev.elysium.eraces.abilities.abils.buffs.self_buffs

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.AbilityUtils
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import org.bukkit.attribute.Attribute
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.potion.PotionEffectType

@RegisterAbility
@Suppress("unused")
class DestructiveRageAbility : BaseEffectsAbility(
    "destructiverage", "70s",
    defaultEffects = mapOf(
        "speed" to EffectData("20s", 1, PotionEffectType.SPEED),
        "jump_boost" to EffectData("20s", 1, PotionEffectType.JUMP_BOOST),
    )) {

    var additionalArmor = 3.0
    var armorDuraton = "20s"

    override fun customActivate(player: Player) {
        addArmor(player)

        // убираем броню через 20 секунд
        AbilityUtils.runLater(ERaces.getInstance(), armorDuraton) {
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

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("additionalArmor", ::additionalArmor)
            read("armorDuraton", ::armorDuraton)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("additionalArmor", additionalArmor)
            write("armorDuraton", armorDuraton)
        }
    }
}