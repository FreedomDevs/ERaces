package dev.elysium.eraces.items.weapon

import org.bukkit.Material

/**
 * Абстрактный класс для оружия дальнего боя.
 *
 * @property id Уникальный идентификатор оружия.
 * @property material Материал предмета в Minecraft, из которого создается оружие.
 * @property name Отображаемое имя оружия.
 * @property damage Базовый урон оружия.
 * @property attackSpeed Скорость атаки оружия.
 * @property isUnbreakable Определяет, является ли оружие неразрушимым.
 * @property maxDurability Максимальная прочность оружия (количество ударов до поломки).
 * @property options Дополнительные настройки оружия (например, "critChance", "lore" и другие параметры).
 */
abstract class RangeWeapon(
    override val id: String,
    override val material: Material,
    override val name: String,
    override val damage: Double,
    override val attackSpeed: Double,
    override val isUnbreakable: Boolean = false,
    override val maxDurability: Int,
    override val options: Map<String, Any> = emptyMap()
) : Weapon(id, material, name, damage, attackSpeed, isUnbreakable, maxDurability, options) {

}