package dev.elysium.eraces.utils.targetUtils

import dev.elysium.eraces.utils.targetUtils.target.TargetExecutor
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import org.bukkit.entity.LivingEntity

/**
 * Расширения для LivingEntity для удобного применения эффектов.
 * Делегируют работу объекту TargetExecutor.
 */

/**
 * Поджигает сущность на указанное количество тиков.
 * Делегирует вызов TargetExecutor.ignite.
 *
 * @param ticks Время поджигания в тиках (1 секунда = 20 тиков)
 */
fun LivingEntity.ignite(ticks: Int) = TargetExecutor.ignite(this, ticks)

/**
 * Наносит урон сущности.
 * Делегирует вызов TargetExecutor.damage.
 *
 * @param amount Количество урона (не может быть меньше 0)
 * @param source Источник урона (опционально)
 */
fun LivingEntity.safeDamage(amount: Double, source: LivingEntity? = null) = TargetExecutor.damage(this, amount, source)

/**
 * Замедляет сущность с указанной длительностью и усилением.
 * Делегирует вызов TargetExecutor.slow.
 *
 * @param duration Длительность эффекта в тиках
 * @param amplifier Уровень эффекта замедления (0 = I, 1 = II и т.д.)
 */
fun LivingEntity.slow(duration: Int, amplifier: Int) = TargetExecutor.slow(this, duration, amplifier)

/**
 * Применяет отдачу к сущности.
 * Делегирует вызов TargetExecutor.knockback.
 *
 * @param power Сила отдачи (положительное число)
 */
fun LivingEntity.knockback(power: Double) = TargetExecutor.knockback(this, power)

/**
 * Восстанавливает здоровье сущности.
 * Делегирует вызов TargetExecutor.heal.
 *
 * @param amount Количество восстанавливаемого здоровья (не превышает максимальное)
 */
fun LivingEntity.safeHeal(amount: Double) = TargetExecutor.heal(this, amount)


/**
 * Проверяет, включен ли фильтр ENTITIES в набор фильтров.
 * @receiver Set<TargetFilter> — набор фильтров
 * @return true, если ENTITIES присутствует
 */
fun Set<TargetFilter>.includesEntities() = TargetFilter.ENTITIES in this

/**
 * Проверяет, включен ли фильтр STOP_AT_BLOCK в набор фильтров.
 * @receiver Set<TargetFilter> — набор фильтров
 * @return true, если STOP_AT_BLOCK присутствует
 */
fun Set<TargetFilter>.stopsAtBlock() = TargetFilter.STOP_AT_BLOCK in this

/**
 * Дружненько забили хуй оно надо поверьте
 * Вам это никогда не пригодится
 */
private fun TargetFilter.isEntityFilter() = this == TargetFilter.FIRST_ENTITY

fun Set<TargetFilter>.shouldCollectEntities() =
    TargetFilter.ENTITIES in this || TargetFilter.FIRST_ENTITY in this

private fun Set<TargetFilter>.shouldStopAtBlock() =
    TargetFilter.STOP_AT_BLOCK in this
