package dev.elysium.eraces.utils.targetUtils

import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import dev.elysium.eraces.utils.targetUtils.target.TargetRaycast
import dev.elysium.eraces.utils.targetUtils.target.TargetTrail
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

/**
 * Основной API таргетинга.
 * Позволяет искать сущности и блоки вокруг игрока или сущности, выполнять raycast и визуализировать лучи.
 */
class Target private constructor(
    private val entities: List<LivingEntity>,
    private val blocks: List<Block> = emptyList(),
    private val caster: LivingEntity? = null,
    private val filters: Set<TargetFilter> = emptySet()
) {

    companion object {
        /**
         * Создает Target на основе игрока.
         * @param player Игрок, от лица которого производится таргетинг
         * @return Новый объект Target с игроком как кастером
         */
        fun from(player: Player) = Target(listOf(player), caster = player)

        /**
         * Создает Target на основе любой LivingEntity.
         * @param entity Сущность, от лица которой производится таргетинг
         * @return Новый объект Target с сущностью как кастером
         */
        fun from(entity: LivingEntity) = Target(listOf(entity), caster = entity)

        /**
         * Создает Target без сущностей, только для позиции.
         * @param location Локация для таргетинга
         * @return Новый объект Target без кастера
         */
        fun from(location: Location) = Target(emptyList(), caster = null)
    }

    /**
     * Добавляет фильтры для поиска целей.
     * @param newFilters Перечисление TargetFilter
     * @return Новый объект Target с обновленными фильтрами
     */
    fun filter(vararg newFilters: TargetFilter) =
        copy(filters = filters + newFilters)

    /**
     * Находит сущности в радиусе вокруг кастера.
     * Может использовать простой поиск через getNearbyEntities или точный raycast.
     * @param radius Радиус поиска
     * @param useRaycast Если true — поиск через raycast, иначе через getNearbyEntities
     * @param step Шаг для raycast (только при useRaycast = true)
     * @return Новый объект Target с найденными сущностями
     */
    fun inRadius(radius: Double, useRaycast: Boolean = false, step: Double = 0.3): Target {
        val caster = this.caster ?: return this
        return if (!useRaycast) {
            val nearby = caster.world.getNearbyEntities(caster.location, radius, radius, radius)
                .filterIsInstance<LivingEntity>()
                .filter { it != caster }
            copy(entities = nearby)
        } else {
            val foundEntities = mutableSetOf<LivingEntity>()
            raycast(RaycastConfig().apply {
                this.distance = radius
                this.step = step
                this.filters = filters
                this.onHit = { foundEntities.add(it) }
            })
            copy(entities = foundEntities.toList())
        }
    }

    /**
     * Находит сущности и блоки по линии взгляда кастера.
     * @param distance Дальность трассировки
     * @param step Шаг raycast
     * @return Новый объект Target с найденными сущностями и блоками
     */
    fun inEye(distance: Double, step: Double = 0.3): Target =
        raycast(RaycastConfig().apply {
            this.distance = distance
            this.step = step
            this.filters = filters
        })

    /**
     * Выполняет низкоуровневый raycast с указанной конфигурацией.
     * @param config Конфигурация raycast (дистанция, шаг, фильтры, onHit, onStep)
     * @return Новый объект Target с найденными сущностями и блоками
     */
    fun raycast(config: RaycastConfig): Target {
        val caster = this.caster ?: return this
        val activeFilters = if (config.filters.isNotEmpty()) config.filters else filters

        val (foundEntities, foundBlocks) = TargetRaycast.raycast(
            caster, config.distance, config.step, activeFilters
        )
        foundEntities.forEach(config.onHit)
        return copy(entities = foundEntities, blocks = foundBlocks)
    }

    /**
     * Визуализирует луч с частицами и одновременно собирает цели.
     * @param config Конфигурация TargetTrail (частицы, шаг, дальность, onStep, onHit)
     * @return Новый объект Target с найденными сущностями и блоками
     */
    fun trail(config: TargetTrail.Config): Target {
        val target = raycast(RaycastConfig().apply {
            this.distance = config.distance
            this.step = config.step
            this.filters = filters
            this.onStep = config.onStep!!
            this.onHit = config.onHit
        })
        caster?.let {
            if (it is Player) TargetTrail.cast(it, config)
        }
        return target
    }

    /**
     * Исключает кастера из списка найденных сущностей.
     * @return Новый объект Target без кастера
     */
    fun excludeCaster(): Target {
        val caster = this.caster ?: return this
        return copy(entities = entities.filter { it.uniqueId != caster.uniqueId })
    }

    /**
     * Применяет действие ко всем найденным сущностям.
     * @param action Lambda-функция, которая принимает LivingEntity
     * @return Тот же объект Target для цепочек вызовов
     */
    fun execute(action: (LivingEntity) -> Unit): Target {
        entities.forEach(action)
        return this
    }

    /**
     * Возвращает список найденных сущностей.
     * @return Список LivingEntity
     */
    fun getEntities() = entities

    /**
     * Возвращает список найденных блоков.
     * @return Список Block
     */
    fun getBlocks() = blocks

    /**
     * Создает копию Target с возможностью замены отдельных параметров.
     * @param entities Список сущностей (по умолчанию текущий)
     * @param blocks Список блоков (по умолчанию текущий)
     * @param caster Кастер (по умолчанию текущий)
     * @param filters Фильтры (по умолчанию текущие)
     * @return Новый объект Target с указанными изменениями
     */
    private fun copy(
        entities: List<LivingEntity> = this.entities,
        blocks: List<Block> = this.blocks,
        caster: LivingEntity? = this.caster,
        filters: Set<TargetFilter> = this.filters
    ) = Target(entities, blocks, caster, filters)
}
