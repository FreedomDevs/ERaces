package dev.elysium.eraces.utils.targetUtils.target

import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
 * Универсальные действия для LivingEntity с безопасными проверками.
 *
 * ⚠️ Не вызывать напрямую! Использовать через расширения LivingEntity:
 * ignite(), safeDamage(), slow(), knockback(), safeHeal()
 *
 * Все методы гарантируют, что параметры не будут отрицательными
 * и предотвращают ошибки при применении эффектов.
 */
object TargetExecutor {

    /**
     * Поджигает сущность на указанное количество тиков.
     * @param entity Цель для поджигания
     * @param ticks Длительность поджигания в тиках (1 секунда = 20 тиков, минимальное значение 0)
     */
    fun ignite(entity: LivingEntity, ticks: Int) {
        val safeTicks = ticks.coerceAtLeast(0)
        entity.fireTicks = safeTicks
    }

    /**
     * Наносит урон сущности.
     * @param entity Цель для нанесения урона
     * @param amount Количество урона (минимум 0)
     * @param source Источник урона (опционально)
     */
    fun damage(entity: LivingEntity, amount: Double, source: LivingEntity? = null) {
        val safeAmount = amount.coerceAtLeast(0.0)
        if (safeAmount > 0.0) {
            entity.damage(safeAmount, source)
        }
    }

    /**
     * Замедляет сущность с указанной длительностью и усилением.
     * @param entity Цель для замедления
     * @param duration Длительность эффекта в тиках (минимум 0)
     * @param amplifier Уровень эффекта замедления (минимум 0)
     */
    fun slow(entity: LivingEntity, duration: Int, amplifier: Int) {
        val safeDuration = duration.coerceAtLeast(0)
        val safeAmplifier = amplifier.coerceAtLeast(0)
        if (safeDuration > 0) {
            entity.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, safeDuration, safeAmplifier))
        }
    }

    /**
     * Применяет отдачу к сущности.
     * @param entity Цель для knockback
     * @param power Сила отдачи (минимум 0)
     */
    fun knockback(entity: LivingEntity, power: Double) {
        val safePower = power.coerceAtLeast(0.0)
        if (safePower > 0.0) {
            val dir = entity.location.direction.clone().multiply(-safePower)
            entity.velocity = dir
        }
    }

    /**
     * Восстанавливает здоровье сущности.
     * @param entity Цель для лечения
     * @param amount Количество восстанавливаемого здоровья (минимум 0, не превышает maxHealth)
     */
    fun heal(entity: LivingEntity, amount: Double) {
        val safeAmount = amount.coerceAtLeast(0.0)
        if (safeAmount > 0.0) {
            val newHealth = (entity.health + safeAmount).coerceAtMost(entity.maxHealth)
            entity.health = newHealth
        }
    }
}
