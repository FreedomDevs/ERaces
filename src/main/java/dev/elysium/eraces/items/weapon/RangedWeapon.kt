package dev.elysium.eraces.items.weapon

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.ItemResolver
import org.bukkit.Material
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class RangedWeapon(
    override val id: String,
    val material: Material,
    private val name: String,
    val damage: Double,
    val attackSpeed: Double,
    private val isUnbreakable: Boolean = false,
    val maxDurability: Int,
    val options: Map<String, Any> = emptyMap()
) : Weapon() {

    abstract val plugin: ERaces

    /*
        =========================
            ARROW SYSTEM
        =========================
    */

    open fun consumeArrow(
        player: Player,
        weapon: ItemStack
    ): Boolean {

        if (player.inventory.contains(Material.ARROW))
            return true

        val offhand =
            player.inventory.itemInOffHand

        val item =
            ItemResolver.resolve(offhand)

        if (item !is Quiver)
            return false

        return item.consumeArrow(
            player,
            offhand
        )
    }

    open fun modifyArrow(
        player: Player,
        arrow: Arrow
    ) {

        val offhand =
            player.inventory.itemInOffHand

        val item =
            ItemResolver.resolve(offhand)

        if (item !is Quiver)
            return

        item.modifyArrow(
            player,
            arrow
        )
    }
}