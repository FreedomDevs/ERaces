package dev.elysium.eraces.utils

import dev.elysium.eraces.ERaces
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup

object WeaponAttributeUtils {
    data class WeaponAttributes(
        val damageModifier: AttributeModifier,
        val attackSpeedModifier: AttributeModifier
    )

    /**
     * Создаёт AttributeModifier для скорости атаки, чтобы итоговая скорость была ровно attackSpeed
     */
    fun createAttackSpeedModifier(
        plugin: ERaces,
        id: String,
        material: Material,
        attackSpeed: Double
    ): AttributeModifier {
        return AttributeModifier(
            NamespacedKey(plugin, "${id}_speed"),
            -attackSpeed,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )
    }

    /**
     * Создаёт AttributeModifier для урона, чтобы итоговый урн был ровно damage
     */
    fun createDamageModifier(
        plugin: ERaces,
        id: String,
        material: Material,
        damage: Double
    ): AttributeModifier {
        return AttributeModifier(
            NamespacedKey(plugin, "${id}_damage"),
            damage,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )
    }

    fun createWeaponAttributes(
        plugin: ERaces,
        id: String,
        material: Material,
        damage: Double,
        attackSpeed: Double
    ): WeaponAttributes {
        val damageModifier = createDamageModifier(plugin, id, material, damage)
        val speedModifier = createAttackSpeedModifier(plugin, id, material, attackSpeed)
        return WeaponAttributes(damageModifier, speedModifier)
    }
}