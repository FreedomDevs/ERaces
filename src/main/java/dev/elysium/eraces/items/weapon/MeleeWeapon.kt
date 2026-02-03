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
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack

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
        val mana = state.getInt(StateKeys.MANA)

        val loreLines = (options["lore"] as? List<*>)?.mapNotNull {
            it as? String
        } ?: emptyList()

        val updateLore = loreLines.map { line ->
            ChatUtil.parse(
                line, mapOf(
                    "{hits}" to hits.toString(),
                    "{durability}" to max.toString(),
                    "{bar}" to getDurabilityBar(hits, max),
                    "{current_durability}" to (max - hits).toString(),
                    "{mana}" to mana.toString()
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

    /**
     * Пытается активировать способность оружия с учётом перезарядки (cooldown).
     *
     * Если способность готова к использованию:
     * 1. Выполняется лямбда [onActivate].
     * 2. Устанавливается время перезарядки для оружия.
     *
     * Если способность ещё в перезарядке, игрок получает сообщение о оставшемся времени ожидания.
     *
     * @param player Игрок, который активирует способность.
     * @param stack [ItemStack] оружия, для которого проверяется и устанавливается перезарядка.
     * @param cooldown Время перезарядки в миллисекундах.
     * @param onActivate Лямбда, которая выполняется при успешной активации способности.
     *
     * Пример использования:
     * ```
     * tryAbility(player, weaponStack, 5000L) {
     *     // Действие при успешной активации
     *     player.actionMsg("<green>Способность активирована!")
     * }
     * ```
     *
     * Поведение функции:
     * - Проверяет текущее время и оставшийся кулдаун.
     * - Если кулдаун ещё не завершён, сообщает игроку время ожидания.
     * - В противном случае выполняет [onActivate] и обновляет время кулдауна.
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

    /**
     * Утилита для построения лора оружия в виде списка строк.
     *
     * Лор содержит:
     * - базовые характеристики оружия: урон, скорость атаки, прочность, мана;
     * - активные способности с названием и описанием;
     * - пассивные эффекты с названием и описанием.
     *
     * Для базовой информации используется функция [base], которая возвращает список строк [MutableList<String>].
     * Активные способности добавляются с помощью [activeAbility], пассивные эффекты — через [passiveEffect].
     *
     * Полный лор создается с помощью функции [build], которая объединяет все части и возвращает [List<String>].
     *
     * При создании лора поддерживаются следующие возможности:
     * - использование плейсхолдеров для текущей и максимальной прочности ({current_durability}, {durability});
     * - возможность отображения визуальной шкалы прочности ({bar});
     * - возможность отображения информации о мане.
     *
     * Все строки лора форматируются с использованием символов:
     * - ⚔ — иконка урона;
     * - ⚡ — иконка скорости атаки;
     * - ⛏ — иконка прочности;
     * - ✦ — иконка активной или пассивной способности.
     *
     * Примеры использования:
     * ```
     * val lore = WeaponLoreBuilder.build(
     *     damage = 10.0,
     *     attackSpeed = 1.5,
     *     activeAbilities = listOf("Огненный удар" to listOf("Поджигает врагов")),
     *     passiveEffects = listOf("Ядовитые клинки" to listOf("Наносит 2 урона в секунду"))
     * )
     * ```
     */
    protected object WeaponLoreBuilder {
        private const val DAMAGE_ICON = "⚔"
        private const val SPEED_ICON = "⚡"
        private const val DURABILITY_ICON = "⛏"
        private const val ACTIVE_ICON = "✦"
        private const val PASSIVE_ICON = "✦"

        /**
         * Создает базовую часть лора оружия.
         *
         * Включает:
         * - Урон и скорость атаки;
         * - Дополнительную информацию из [extraInfo];
         * - Прочность и визуальную шкалу прочности;
         * - Информацию о мане, если [useMana] = true.
         *
         * @param damage Значение урона оружия.
         * @param attackSpeed Скорость атаки оружия.
         * @param extraInfo Список дополнительных строк информации (по умолчанию пустой).
         * @param currentDurabilityPlaceholder Плейсхолдер для текущей прочности (по умолчанию "{current_durability}").
         * @param maxDurabilityPlaceholder Плейсхолдер для максимальной прочности (по умолчанию "{durability}").
         * @param barPlaceholder Плейсхолдер для визуальной шкалы прочности (по умолчанию "{bar}").
         * @param useMana Если true, добавляет строку с информацией о мане (по умолчанию false).
         *
         * @return [MutableList] строк лора базовых характеристик.
         *
         * Пример использования:
         * ```
         * val baseLore = WeaponLoreBuilder.base(10.0, 1.5, listOf("Особое свойство"))
         * ```
         */
        fun base(
            damage: Double,
            attackSpeed: Double,
            extraInfo: List<String> = emptyList(),
            currentDurabilityPlaceholder: String = "{current_durability}",
            maxDurabilityPlaceholder: String = "{durability}",
            barPlaceholder: String = "{bar}",
            useMana: Boolean = false
        ): MutableList<String> {
            val result = mutableListOf(
                "<gray>$DAMAGE_ICON <white>Урон: <red>$damage",
                "<gray>$SPEED_ICON <white>Скорость атаки: <yellow>$attackSpeed"
            )
            if (extraInfo.isNotEmpty()) {
                result += extraInfo
            }

            result += ""
            if (useMana) {
                result += "<white>Мана: <yellow>{mana}"
            }
            result += "<gray>$DURABILITY_ICON <white>Прочность:"
            result += "<gray>[ <white>$currentDurabilityPlaceholder<gray> / <white>$maxDurabilityPlaceholder <gray>]"
            result += barPlaceholder
            return result
        }

        /**
         * Создает лор для активной способности оружия.
         *
         * @param name Название способности.
         * @param description Список строк с описанием способности.
         *
         * @return [List] строк лора для активной способности.
         *
         * Пример использования:
         * ```
         * val activeLore = WeaponLoreBuilder.activeAbility("Огненный удар", listOf("Наносит 10 урона", "Поджигает врагов"))
         * ```
         */
        fun activeAbility(
            name: String,
            description: List<String>
        ): List<String> {
            return listOf("<gold>$ACTIVE_ICON $name</gold>") + description
        }

        /**
         * Создает лор для пассивного эффекта оружия.
         *
         * @param name Название пассивного эффекта.
         * @param description Список строк с описанием эффекта.
         *
         * @return [List] строк лора для пассивного эффекта.
         *
         * Пример использования:
         * ```
         * val passiveLore = WeaponLoreBuilder.passiveEffect("Ядовитые клинки", listOf("Наносит 2 урона в секунду"))
         * ```
         */
        fun passiveEffect(
            name: String,
            description: List<String>
        ): List<String> {
            return listOf("<aqua>$PASSIVE_ICON $name</aqua>") + description
        }

        /**
         * Создает полный лор оружия, объединяя базовые характеристики, активные способности и пассивные эффекты.
         *
         * @param damage Значение урона оружия.
         * @param attackSpeed Скорость атаки оружия.
         * @param activeAbilities Список пар (название, описание) активных способностей.
         * @param passiveEffects Список пар (название, описание) пассивных эффектов.
         * @param extraBaseInfo Дополнительные строки для базового лора.
         * @param currentDurabilityPlaceholder Плейсхолдер для текущей прочности.
         * @param maxDurabilityPlaceholder Плейсхолдер для максимальной прочности.
         * @param barPlaceholder Плейсхолдер для визуальной шкалы прочности.
         * @param useMana Если true, добавляет строку с информацией о мане.
         *
         * @return [List] строк полного лора оружия.
         *
         * Пример использования:
         * ```
         * val fullLore = WeaponLoreBuilder.build(
         *     damage = 12.0,
         *     attackSpeed = 1.8,
         *     activeAbilities = listOf("Огненный удар" to listOf("Поджигает врагов")),
         *     passiveEffects = listOf("Ядовитые клинки" to listOf("Наносит 2 урона в секунду"))
         * )
         * ```
         */
        fun build(
            damage: Double,
            attackSpeed: Double,
            activeAbilities: List<Pair<String, List<String>>> = emptyList(),
            passiveEffects: List<Pair<String, List<String>>> = emptyList(),
            extraBaseInfo: List<String> = emptyList(),
            currentDurabilityPlaceholder: String = "{current_durability}",
            maxDurabilityPlaceholder: String = "{durability}",
            barPlaceholder: String = "{bar}",
            useMana: Boolean = false
        ): List<String> {
            val lore = mutableListOf<String>()

            lore += base(
                damage,
                attackSpeed,
                extraBaseInfo,
                currentDurabilityPlaceholder,
                maxDurabilityPlaceholder,
                barPlaceholder,
                useMana
            )
            lore += ""

            passiveEffects.forEach { (name, desc) ->
                lore += passiveEffect(name, desc)
                lore += ""
            }

            activeAbilities.forEach { (name, desc) ->
                lore += activeAbility(name, desc)
                lore += ""
            }

            return lore
        }
    }
}
