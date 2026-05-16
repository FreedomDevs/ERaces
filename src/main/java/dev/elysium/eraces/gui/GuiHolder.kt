package dev.elysium.eraces.gui

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class GuiHolder(val guiType: GuiType, val id: String = "") : InventoryHolder {

    private lateinit var inventory: Inventory

    fun setInventory(inventory: Inventory) {
        this.inventory = inventory
    }

    override fun getInventory(): Inventory {
        return inventory
    }
}