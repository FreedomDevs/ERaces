package dev.elysium.eraces.items.weapon

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.ItemResolver
import dev.elysium.eraces.items.core.state.ItemState
import dev.elysium.eraces.items.core.state.StateKeys
import dev.elysium.eraces.utils.WeaponAttributeUtils
import dev.elysium.eraces.utils.ChatUtil
import dev.elysium.eraces.utils.actionMsg
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

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
@Suppress("UnstableApiUsage")
abstract class MeleeWeapon(
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

    /**
     * Инициализирует предмет, задавая его атрибуты, имя, прочность и отображаемый текст.
     *
     * @param item ItemStack, который нужно инициализировать.
     */
    override fun onInit(item: ItemStack) {
        val attributes = WeaponAttributeUtils.createWeaponAttributes(
            plugin = plugin,
            id = id,
            material = material,
            damage = damage,
            attackSpeed = attackSpeed
        )

        item.setData(
            DataComponentTypes.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.itemAttributes()
                .addModifier(Attribute.ATTACK_DAMAGE, attributes.damageModifier)
                .addModifier(Attribute.ATTACK_SPEED, attributes.attackSpeedModifier)
                .build()
        )

        if (isUnbreakable)
            item.setData(DataComponentTypes.UNBREAKABLE)

        item.setData(DataComponentTypes.CUSTOM_NAME, ChatUtil.parse(name));

        item.setData(
            DataComponentTypes.CUSTOM_MODEL_DATA,
            CustomModelData.customModelData().addString(id).build()
        );

        val state = ItemState(item)

        if (!state.contains(StateKeys.DURABILITY)) {
            state.setInt(StateKeys.DURABILITY, maxDurability)
        }
        if (!state.contains(StateKeys.HITS)) {
            state.setInt(StateKeys.HITS, 0)
        }

        updateLore(item)
    }

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
            player.sendMessage("Ваш ${stack.itemMeta?.displayName} сломался!")
            resolveBrokenItem(player, stack)
        }

        val critChance = options["critChance"] as? Double ?: 0.0
        if (Math.random() < critChance) {
            event.damage *= 2
            player.sendMessage(ChatUtil.parse("&6Критический удар!"))
        }
    }

    /**
     * Обновляет описание (lore) предмета, включая прочность, количество ударов и индикатор прочности.
     *
     * @param item [ItemStack], для которого обновляется lore.
     */
    private fun updateLore(item: ItemStack) {
        val meta = item.itemMeta ?: return
        val state = ItemState(item)
        val hits = state.getInt(StateKeys.HITS)
        val max = state.getInt(StateKeys.DURABILITY)

        val loreLines = (options["lore"] as? List<*>)?.mapNotNull {
            it as? String
        } ?: emptyList()

        val updateLore = loreLines.map { line ->
            ChatUtil.parse(
                line, mapOf(
                    "{hits}" to hits.toString(),
                    "{durability}" to max.toString(),
                    "{bar}" to getDurabilityBar(hits, max),
                    "{current_durability}" to (max - hits).toString()
                )
            )
        }

        meta.lore(updateLore)
        item.itemMeta = meta

    }

    /**
     * Удаляет предмет из инвентаря игрока, если он сломался.
     *
     * @param player Игрок, который держит сломанный предмет.
     * @param stack Сломанный [ItemStack].
     */
    private fun resolveBrokenItem(player: Player, stack: ItemStack) {
        val inv = player.inventory

        for (slot in 0 until inv.size) {
            val current = inv.getItem(slot) ?: continue

            if (current.isSimilar(stack)) {
                inv.setItem(slot, null)
                return
            }
        }

        if (inv.itemInOffHand.isSimilar(stack)) {
            inv.setItemInOffHand(null)
        }
    }

    /**
     * Генерирует визуальный индикатор прочности оружия в виде строки.
     *
     * @param hits Количество использованных ударов.
     * @param max Максимальная прочность.
     * @param length Длина индикатора (по умолчанию 10).
     * @return Строка с индикатором прочности, где заполненная часть красная, а пустая — зеленая.
     */
    private fun getDurabilityBar(hits: Int, max: Int, length: Int = 10): String {
        val safeHits = hits.coerceAtMost(max)
        val percent = safeHits.toDouble() / max
        val filled = (length * percent).toInt()
        val empty = (length - filled).coerceAtLeast(0)

        val barFilled = "■".repeat(filled)
        val barEmpty = "■".repeat(empty)

        val bar = "<red>$barFilled<green>$barEmpty"

        return bar
    }

    /**
     * Ищет ближайшую живую цель в указанном направлении и диапазоне.
     *
     * @param player Игрок, от которого ведется поиск.
     * @param range Дальность поиска.
     * @param predicate Дополнительное условие для фильтрации целей.
     * @return Первую подходящую цель [LivingEntity] или null, если ничего не найдено.
     */
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

    /**
     * Пытается активировать способность оружия с учётом перезарядки.
     * Если успешно выполняет onActivate и добовляет кулдаун.
     *
     * @param player Игрок, который активирует способность.
     * @param stack ItemStack оружия, для которого проверяется способность.
     * @param cooldown Время перезарядки в миллисекундах.
     * @param onActivate Лямбда, которая выполняется при успешной активации способности.
     */
    protected fun tryAbility(player: Player, stack: ItemStack, cooldown: Long, onActivate: () -> Unit) {
        val state = ItemState(stack)
        val now = System.currentTimeMillis()
        val kdEnd = state.getLong(StateKeys.KD)

        if (now < kdEnd) {
            val remaining = (kdEnd - now) / 1000.0
            player.actionMsg("<red>Способность еще не готова! <gold>${"%.1f".format(remaining)}s</gold>")
            return
        }

        onActivate()
        state.setLong(StateKeys.KD, now + cooldown)
    }
}
