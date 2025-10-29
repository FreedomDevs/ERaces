package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.ConfigHelper.read
import dev.elysium.eraces.abilities.ConfigHelper.write
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionType

class ArsenalAbility : BaseCooldownAbility(id = "arsenal", defaultCooldown = "5m") {
    private var effects: String = "POISON;SLOWNESS"
    private var count: Int = 16

    private var parsedEffects = parseEffects()

    private fun parseEffects(): List<PotionType> {
        val output: MutableList<PotionType> = mutableListOf()
        for (i in effects.split(";"))
            output.add(PotionType.valueOf(i.uppercase()))

        return output
    }

    override fun onActivate(player: Player) {
        val effect = parsedEffects.random()
        val item = ItemStack(Material.TIPPED_ARROW, count)
        val meta = item.itemMeta as PotionMeta

        meta.basePotionType = effect
        item.itemMeta = meta

        player.give(item)
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("effects", ::effects)
            read("count", ::count)
        }
        parsedEffects = parseEffects()
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("effects", effects)
            write("count", count)
        }
    }
}