package dev.elysium.eraces.items.weapon

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

abstract class MeleeWeapon(
    override val id: String,
    private val material: Material,
    private val model: Int,
    private val name: String,
    private val damage: Double,
    private val attackSpeed: Double
) : Weapon() {

    override fun onInit(item: ItemStack) {
        val meta = item.itemMeta ?: return
        meta.setDisplayName(name)
        meta.setCustomModelData(model)
        item.itemMeta = meta
    }
}
