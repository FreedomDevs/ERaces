package dev.elysium.eraces.items.weapon

import dev.elysium.eraces.items.core.ItemResolver
import dev.elysium.eraces.items.core.state.ItemState
import dev.elysium.eraces.items.core.state.StateKeys
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

/**
 * Абстрактный класс для оружия ближнего боя.
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
abstract class MeleeWeapon(
    override val id: String,
    override val material: Material,
    override val name: String,
    override val damage: Double,
    override val attackSpeed: Double,
    override val isUnbreakable: Boolean = false,
    override val maxDurability: Int,
    override val options: Map<String, Any> = emptyMap()
) : Weapon(id, material, name, damage, attackSpeed, isUnbreakable, maxDurability, options) {
    /**
     * Вызывается при нанесении урона сущностью.
     *
     * Выполняет следующие действия:
     *  - Увеличивает количество ударов оружия.
     *  - Проверяет, не сломалось ли оружие.
     *  - Применяет критический урон, если выпал шанс.
     *
     * @param event Событие [EntityDamageByEntityEvent].
     */
    override fun onHit(event: EntityDamageByEntityEvent) {
        val player = event.damager as? Player ?: return
        val stack = player.inventory.itemInMainHand
        val weapon = ItemResolver.resolve(stack) as? MeleeWeapon ?: return
        if (weapon.id != this.id) return

        val state = ItemState(stack)
        val hits = state.addInt(StateKeys.HITS, 1)
        val max = state.getInt(StateKeys.DURABILITY)

        updateLore(stack)

        if (hits >= max) {
            player.sendMessage(
                Component.text("Ваш ")
                    .append(stack.itemMeta?.displayName() ?: Component.text("предмет"))
                    .append(Component.text(" сломался!"))
            )
            resolveBrokenItem(player, stack)
        }

        val critChance = options["critChance"] as? Double ?: 0.0
        if (Math.random() < critChance) {
            event.damage *= 2
            player.sendMessage(Component.text("Критический удар!", NamedTextColor.GOLD))
        }
    }

    /**
     * Ищет ближайшую живую цель в указанном направлении и диапазоне.
     *
     * @param player Игрок, от которого ведется поиск.
     * @param range Дальность поиска.
     * @param predicate Дополнительное условие для фильтрации целей.
     * @return Первую подходящую цель [LivingEntity] или null, если ничего не найдено.
     */
    @Deprecated(message = "Данынй метод больше не рекомендуется к использованию")
    protected fun findLivingTarget(
        player: Player,
        range: Double,
        predicate: ((LivingEntity) -> Boolean)? = null
    ): LivingEntity? {

        val result = player.world.rayTraceEntities(
            player.eyeLocation,
            player.eyeLocation.direction,
            range
        ) { entity ->
            entity is LivingEntity &&
                    entity != player &&
                    (predicate?.invoke(entity) ?: true)
        }

        return result?.hitEntity as? LivingEntity
    }
}
