package dev.elysium.eraces.xpManager.providers

import org.bukkit.Material
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.inventory.ItemStack

class CraftXpProvider(
    private val xpMap: Map<Material, Long>,
    private val defaultXp: Long = 0L
) : XpProvider<CraftItemEvent> {

    override fun getXp(target: CraftItemEvent): Long? {
        val result: ItemStack = target.recipe.result
        return xpMap[result.type] ?: defaultXp
    }
}