package dev.elysium.eraces.utils

import dev.elysium.eraces.ERaces
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup

object WeaponAttributeUtils {
    data class BaseWeaponStats(
        val baseDamage: Double,
        val baseAttackSpeed: Double
    )

    data class WeaponAttributes(
        val damageModifier: AttributeModifier,
        val attackSpeedModifier: AttributeModifier
    )

    private val baseStats: Map<Material, BaseWeaponStats> = mapOf(
//        SWORDS
        Material.WOODEN_SWORD to BaseWeaponStats(4.0, 1.6),
        Material.STONE_SWORD to BaseWeaponStats(5.0, 1.6),
        Material.IRON_SWORD to BaseWeaponStats(6.0, 1.6),
        Material.GOLDEN_SWORD to BaseWeaponStats(4.0, 1.6),
        Material.DIAMOND_SWORD to BaseWeaponStats(7.0, 1.6),
        Material.NETHERITE_SWORD to BaseWeaponStats(8.0, 1.6),

//        AXES
        Material.WOODEN_AXE to BaseWeaponStats(7.0, 0.8),
        Material.STONE_AXE to BaseWeaponStats(9.0, 0.8),
        Material.IRON_AXE to BaseWeaponStats(9.0, 0.9),
        Material.GOLDEN_AXE to BaseWeaponStats(7.0, 1.0),
        Material.DIAMOND_AXE to BaseWeaponStats(9.0, 1.0),
        Material.NETHERITE_AXE to BaseWeaponStats(10.0, 1.0),

//        SHOVELS
        Material.WOODEN_SHOVEL to BaseWeaponStats(2.5, 1.0),
        Material.STONE_SHOVEL to BaseWeaponStats(3.5, 1.0),
        Material.IRON_SHOVEL to BaseWeaponStats(4.5, 1.0),
        Material.GOLDEN_SHOVEL to BaseWeaponStats(2.5, 1.0),
        Material.DIAMOND_SHOVEL to BaseWeaponStats(5.5, 1.0),
        Material.NETHERITE_SHOVEL to BaseWeaponStats(6.0, 1.0),

//        PICKAXES
        Material.WOODEN_PICKAXE to BaseWeaponStats(2.0, 1.2),
        Material.STONE_PICKAXE to BaseWeaponStats(3.0, 1.2),
        Material.IRON_PICKAXE to BaseWeaponStats(4.0, 1.2),
        Material.GOLDEN_PICKAXE to BaseWeaponStats(2.0, 1.2),
        Material.DIAMOND_PICKAXE to BaseWeaponStats(5.0, 1.2),
        Material.NETHERITE_PICKAXE to BaseWeaponStats(6.0, 1.2),

//        HOES
        Material.WOODEN_HOE to BaseWeaponStats(1.0, 1.0),
        Material.STONE_HOE to BaseWeaponStats(1.0, 1.0),
        Material.IRON_HOE to BaseWeaponStats(2.0, 1.0),
        Material.GOLDEN_HOE to BaseWeaponStats(1.0, 1.0),
        Material.DIAMOND_HOE to BaseWeaponStats(3.0, 1.0),
        Material.NETHERITE_HOE to BaseWeaponStats(4.0, 1.0),

//        OTHERS
        Material.MACE to BaseWeaponStats(6.0, 0.6),
        Material.TRIDENT to BaseWeaponStats(9.0, 1.1),
    )

    fun getBaseStats(material: Material): BaseWeaponStats {
        return baseStats[material] ?: BaseWeaponStats(1.0, 4.0)
    }

    /**
     * Создаёт AttributeModifier для скорости атаки, чтобы итоговая скорость была ровно attackSpeed
     */
    fun createAttackSpeedModifier(
        plugin: ERaces,
        id: String,
        material: Material,
        attackSpeed: Double
    ): AttributeModifier {
        val baseSpeed = getBaseStats(material).baseAttackSpeed
        return AttributeModifier(
            NamespacedKey(plugin, "${id}_speed"),
            attackSpeed - baseSpeed,
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
        val baseDamage = getBaseStats(material).baseDamage
        return AttributeModifier(
            NamespacedKey(plugin, "${id}_damage"),
            damage - baseDamage,
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