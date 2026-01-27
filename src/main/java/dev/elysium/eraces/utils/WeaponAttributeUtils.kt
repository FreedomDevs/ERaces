package dev.elysium.eraces.utils

import dev.elysium.eraces.ERaces
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup

object WeaponAttributeUtils {
    data class WeaponAttributes(
        val damageClearModifier: AttributeModifier,
        val damageModifier: AttributeModifier,
        val attackSpeedClearModifier: AttributeModifier,
        val attackSpeedModifier: AttributeModifier
    )

    fun createWeaponAttributes(
        plugin: ERaces,
        id: String,
        damage: Double,
        attackSpeed: Double
    ): WeaponAttributes {
        val damageCleanModifier = AttributeModifier(
            NamespacedKey(plugin, "${id}_damage_clear"),
            -1.0,
            AttributeModifier.Operation.ADD_SCALAR,
            EquipmentSlotGroup.HAND
        )
        val damageModifier = AttributeModifier(
            NamespacedKey(plugin, "${id}_damage"),
            damage,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )
        val attackSpeedClearModifier = AttributeModifier(
            NamespacedKey(plugin, "${id}_speed_clear"),
            -1.0,
            AttributeModifier.Operation.ADD_SCALAR,
            EquipmentSlotGroup.HAND
        )
        val attackSpeedModifier = AttributeModifier(
            NamespacedKey(plugin, "${id}_speed"),
            attackSpeed,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )
        return WeaponAttributes(damageCleanModifier, damageModifier, attackSpeedClearModifier, attackSpeedModifier)
    }
}