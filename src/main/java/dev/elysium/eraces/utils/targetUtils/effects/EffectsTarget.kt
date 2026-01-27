package dev.elysium.eraces.utils.targetUtils.effects

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.vectors.RadiusFillBuilder
import dev.elysium.eraces.utils.vectors.Vec3
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitTask

/**
 * Fluent API для частиц и действий.
 * math(...) принимает либо List<Vec3>, либо ()->List<Vec3>, либо RadiusFillBuilder (через builder.buildProvider()).
 *
 * apply() ставит задачу в планировщик (durationTicks и periodTicks), если duration > 1.
 */
class EffectsTarget {

    private var target: Target? = null
    private var executor: Executor = Executor.PLAYER()
    private var particle: Particle = Particle.FLAME
    private var pointsProvider: (() -> List<Vec3>)? = null
    private var actionOnEntity: ((LivingEntity) -> Unit)? = null
    private var durationTicks: Int = 1
    private var periodTicks: Long = 1L
    private var maxPointsPerTick: Int = Int.MAX_VALUE
    private var extra: Double = 0.0
    private var dustOptions: Particle.DustOptions? = null

    /**
     * Устанавливает Target, на который будут применяться эффекты.
     * @param t Target
     * @return текущий EffectsTarget для цепочек вызовов
     */
    fun setTarget(t: Target): EffectsTarget {
        this.target = t; return this
    }

    /**
     * Устанавливает Executor (источник частиц или точку выполнения действия).
     * @param ex Executor (PLAYER или LOC)
     * @return текущий EffectsTarget
     */
    fun from(ex: Executor): EffectsTarget {
        this.executor = ex; return this
    }

    /**
     * Устанавливает значение extra для создаваемых частиц (обычно обозначает скорость частиц)
     * @param extra Double (значение extra)
     * @return текущий EffectsTarget
     */
    fun extra(extra: Double): EffectsTarget {
        this.extra = extra; return this
    }

    /**
     * Устанавливает тип частицы для эффекта.
     * @param p Particle
     * @return текущий EffectsTarget
     */
    fun particle(p: Particle): EffectsTarget {
        this.particle = p; return this
    }

    /**
     * Устанавливает DustOptions (цвет и размер) и particle = Particle.DUST.
     * @param org.bukkit.Particle.DustOptions
     */
    fun dust(options: Particle.DustOptions): EffectsTarget {
        this.particle = Particle.DUST
        this.dustOptions = options
        return this
    }

    /**
     * Задает точки частиц из списка Vec3.
     * @param points список Vec3
     * @return текущий EffectsTarget
     */
    fun math(points: List<Vec3>): EffectsTarget {
        this.pointsProvider = { points }; return this
    }

    /**
     * Задает точки частиц через функцию-поставщик.
     * @param provider функция, возвращающая список Vec3
     * @return текущий EffectsTarget
     */
    fun math(provider: () -> List<Vec3>): EffectsTarget {
        this.pointsProvider = provider; return this
    }

    /**
     * Задает точки частиц через RadiusFillBuilder.
     * @param builder RadiusFillBuilder
     * @return текущий EffectsTarget
     */
    fun math(builder: RadiusFillBuilder): EffectsTarget {
        this.pointsProvider = builder.buildProvider(); return this
    }

    /**
     * Устанавливает действие, которое будет выполняться на каждой сущности Target.
     * @param action лямбда с действием на LivingEntity
     * @return текущий EffectsTarget
     */
    fun action(action: (LivingEntity) -> Unit): EffectsTarget {
        this.actionOnEntity = action; return this
    }

    /**
     * Задает продолжительность эффекта в тиках.
     * @param ticks количество тиков (минимум 1)
     * @return текущий EffectsTarget
     */
    fun duration(ticks: Int): EffectsTarget {
        this.durationTicks = ticks.coerceAtLeast(1); return this
    }

    /**
     * Задает период выполнения эффекта в тиках.
     * @param ticks количество тиков между выполнениями (минимум 1)
     * @return текущий EffectsTarget
     */
    fun period(ticks: Long): EffectsTarget {
        this.periodTicks = ticks.coerceAtLeast(1L); return this
    }

    /**
     * Ограничивает максимальное количество точек частиц за один тик.
     * @param max максимальное количество точек (минимум 1)
     * @return текущий EffectsTarget
     */
    fun limitPoints(max: Int): EffectsTarget {
        this.maxPointsPerTick = max.coerceAtLeast(1); return this
    }

    /**
     * Запускает эффекты и действия на сущностях.
     * Если durationTicks > 1 — ставит runTaskTimer, иначе выполняет один тик.
     * @return Target, к которому применяются эффекты, для цепочек вызовов
     */
    fun apply(): Target {
        val t = target ?: return Target.fromNull()
        val plugin = ERaces.getInstance()
        val targets = t.getEntities()
        val provider = pointsProvider ?: { emptyList<Vec3>() }

        var run = 0L
        var task: BukkitTask? = null

        val runnable = Runnable {
            val points = getLimitedPoints(provider)
            if (points.isNotEmpty()) {
                executeEffects(targets, points)
            }
            actionOnEntity?.let { act -> targets.forEach { act(it) } }

            run += periodTicks
            if (run >= durationTicks) task?.cancel()
        }

        task = scheduleTask(plugin, runnable)

        return t
    }

    // ------ helpers ------

    private fun getLimitedPoints(provider: () -> List<Vec3>): List<Vec3> {
        val points = provider()
        return if (points.size > maxPointsPerTick) points.subList(0, maxPointsPerTick) else points
    }

    private fun executeEffects(targets: List<LivingEntity>, points: List<Vec3>) {
        when (executor) {
            is Executor.PLAYER -> executePlayerExecutor(targets, points)
            is Executor.LOC -> executeLocationExecutor(points)
        }
    }

    private fun executePlayerExecutor(targets: List<LivingEntity>, points: List<Vec3>) {
        val ex = executor as Executor.PLAYER
        if (ex.perEntity) {
            targets.forEach { spawnAtEntity(it, points, ex.offset) }
        } else {
            val originEntity = ex.entity ?: targets.firstOrNull()
            originEntity?.let { spawnAtEntity(it, points, ex.offset) }
        }
    }

    private fun spawnAtEntity(entity: LivingEntity, points: List<Vec3>, offset: Vec3?) {
        val origin = entity.location.clone().add(
            offset?.x ?: 0.0,
            offset?.y ?: (entity.eyeHeight / 4.0),
            offset?.z ?: 0.0
        )
        spawnAt(origin, points)
    }

    private fun executeLocationExecutor(points: List<Vec3>) {
        val loc = (executor as Executor.LOC).location
        spawnAt(loc, points)
    }

    private fun spawnAt(origin: Location, offsets: List<Vec3>) {
        val world = origin.world ?: return

        if (particle == Particle.DUST && dustOptions != null) {
            for (off in offsets) {
                world.spawnParticle(
                    particle,
                    origin.x + off.x,
                    origin.y + off.y,
                    origin.z + off.z,
                    1,
                    0.0, 0.0, 0.0, extra,
                    dustOptions
                )
            }
        } else {
            for (off in offsets) {
                world.spawnParticle(particle, origin.x + off.x, origin.y + off.y, origin.z + off.z, 1, 0.0, 0.0, 0.0, extra)
            }
        }
    }

    private fun scheduleTask(plugin: org.bukkit.plugin.Plugin, runnable: Runnable): BukkitTask {
        return if (durationTicks > 1 || periodTicks > 1L) {
            plugin.server.scheduler.runTaskTimer(plugin, runnable, 0L, periodTicks)
        } else {
            plugin.server.scheduler.runTask(plugin, runnable)
        }
    }
}
