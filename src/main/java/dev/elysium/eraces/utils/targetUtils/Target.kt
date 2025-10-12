package dev.elysium.eraces.utils.targetUtils

import dev.elysium.eraces.utils.targetUtils.effects.EffectsTarget
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import dev.elysium.eraces.utils.targetUtils.target.TargetLogic
import dev.elysium.eraces.utils.targetUtils.target.TargetTrail
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

/**
 * Основной API таргетинга.
 * Позволяет искать сущности и блоки вокруг игрока или сущности, выполнять raycast и визуализировать лучи.
 */
class Target internal constructor(
    private val entities: List<LivingEntity> = emptyList(),
    private val blocks: List<Block> = emptyList(),
    private val caster: LivingEntity? = null,
    private val filters: Set<TargetFilter> = emptySet()
) {

    companion object {
        fun from(player: Player) = Target(listOf(player), caster = player)
        fun from(entity: LivingEntity) = Target(listOf(entity), caster = entity)
        fun from(location: Location) = Target(emptyList(), caster = null)
        fun fromNull() = Target()
    }

    /**
     * Добавляет фильтры к Target.
     * @param newFilters массив фильтров TargetFilter
     * @return новый Target с добавленными фильтрами
     */
    fun filter(vararg newFilters: TargetFilter) = copy(filters = filters + newFilters)

    /**
     * Ищет сущности в радиусе от кастера.
     * @param radius радиус поиска
     * @param useRaycast если true, исключает сущности, скрытые за блоками
     * @param step шаг raycast для проверки блоков
     * @return новый Target с найденными сущностями
     */
    fun inRadius(radius: Double, useRaycast: Boolean = false, step: Double = 0.3): Target =
        caster?.let { radiusSearch(it, radius, useRaycast, step) } ?: this

    /**
     * Создает raycast из глаз кастера на заданное расстояние.
     * @param distance длина raycast
     * @param step шаг проверки точек вдоль луча
     * @return новый Target с найденными сущностями
     */
    fun inEye(distance: Double, step: Double = 0.3): Target =
        TargetLogic.eyeRaycast(caster, filters, distance, step)

    /**
     * Исключает кастера из списка сущностей.
     * @return новый Target без кастера
     */
    fun excludeCaster(): Target =
        caster?.let { copy(entities = entities.filterNot { e -> e.uniqueId == it.uniqueId }) } ?: this

    /**
     * Выполняет указанное действие на всех сущностях Target.
     * @param action лямбда с действием для каждой сущности
     * @return тот же Target для цепочек вызовов
     */
    fun execute(action: (LivingEntity) -> Unit): Target {
        entities.forEach(action)
        return this
    }

    /**
     * Ищет сущности в радиусе с использованием KD-Tree.
     * @param radius радиус поиска
     * @return новый Target с найденными сущностями
     */
    fun inRadiusKDTree(radius: Double): Target =
        caster?.let { Target(TargetLogic.kdTreeRadiusSearch(it, filters, radius), blocks, it, filters) }
            ?: this

    /** Возвращает список найденных сущностей. */
    fun getEntities() = entities

    /** Возвращает список найденных блоков. */
    fun getBlocks() = blocks

    /**
     * Применяет эффекты из EffectsTarget на этот Target.
     * @param effectsTarget экземпляр EffectsTarget
     * @return Target для цепочек вызовов
     */
    fun executeEffects(effectsTarget: EffectsTarget): Target =
        effectsTarget.setTarget(this).apply()

    /**
     * Строит визуальный след (trail) для кастера.
     * @param config конфигурация TargetTrail
     * @return Target для цепочек вызовов
     */
    fun trail(config: TargetTrail.Config): Target {
        caster?.let { TargetTrail.trail(it, config) }
        return this
    }

    private fun copy(
        entities: List<LivingEntity> = this.entities,
        blocks: List<Block> = this.blocks,
        caster: LivingEntity? = this.caster,
        filters: Set<TargetFilter> = this.filters
    ) = Target(entities, blocks, caster, filters)

    private fun radiusSearch(caster: LivingEntity, radius: Double, useRaycast: Boolean, step: Double) =
        TargetLogic.radiusSearch(caster, filters, radius, useRaycast, step)
}
