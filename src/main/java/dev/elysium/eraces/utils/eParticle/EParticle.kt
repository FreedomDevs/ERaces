package dev.elysium.eraces.utils.eParticle

import dev.elysium.eraces.ERaces
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

object EParticle {

    fun spawn(world: World, location: Location, config: ParticleConfig) {
        if (config.direction != null && config.distance != null) {
            spawnLine(world, location, config)
        } else {
            world.spawnParticle(
                config.particle,
                location,
                config.count,
                config.offsetX,
                config.offsetY,
                config.offsetZ,
                config.extra
            )
        }
    }

    private fun spawnLine(world: World, start: Location, config: ParticleConfig) {
        var traveled = 0.0
        while (traveled <= (config.distance ?: 1.0)) {
            val point = start.clone().add(config.direction!!.clone().multiply(traveled))
            world.spawnParticle(
                config.particle,
                point,
                config.count,
                config.offsetX,
                config.offsetY,
                config.offsetZ,
                config.extra
            )
            traveled += config.step
        }
    }

    // ==================== DSL для удобного вызова ====================
    fun World.particles(location: Location, builder: ParticleConfig.Builder.() -> Unit) {
        val config = ParticleConfig.Builder(Particle.SMOKE).apply(builder).build()
        spawn(this, location, config)
    }

    // ==================== Стандартные эффекты ====================

    /** Свайп */
    fun swipe(world: World, location: Location, count: Int = 1) {
        spawn(world, location, ParticleConfig.Builder(Particle.SWEEP_ATTACK).count(count).build())
        spawn(world, location, ParticleConfig.Builder(Particle.CLOUD).count(count).extra(0.01).build())
    }

    /** Критический удар */
    fun crit(world: World, entity: LivingEntity, count: Int = 15, spread: Double = 0.3, extra: Double = 0.2) {
        val loc = entity.location.clone().add(0.0, entity.height * 0.5, 0.0)
        spawn(world, loc, ParticleConfig.Builder(Particle.CRIT)
            .count(count)
            .offset(spread, spread, spread)
            .extra(extra)
            .build()
        )
    }

    /** Простое облако */
    fun cloud(world: World, location: Location, count: Int = 5, speed: Double = 0.01) {
        spawn(world, location, ParticleConfig.Builder(Particle.WHITE_SMOKE)
            .count(count)
            .extra(speed)
            .build()
        )
    }

    /** Индикатор урона / лечение */
    fun damageIndicator(world: World, location: Location, count: Int = 10) {
        val loc = location.clone().add(0.0, 1.0, 0.0)
        spawn(world, loc, ParticleConfig.Builder(Particle.DAMAGE_INDICATOR)
            .count(count)
            .offset(0.3, 0.4, 0.3)
            .extra(0.1)
            .build()
        )
    }

    /** Линейный эффект */
    fun lineEffect(
        world: World,
        start: Location,
        direction: Vector,
        distance: Double,
        step: Double = 0.2,
        particle: Particle = Particle.SMOKE
    ) {
        spawn(world, start, ParticleConfig.Builder(particle)
            .direction(direction)
            .distance(distance)
            .step(step)
            .build()
        )
    }

    // ==================== Повторяющиеся эффекты ====================
    fun repeatEffect(plugin: ERaces, world: World, location: Location, config: ParticleConfig, times: Int, delayTicks: Long) {
        object : BukkitRunnable() {
            var count = 0
            override fun run() {
                if (count++ >= times) cancel()
                spawn(world, location, config)
            }
        }.runTaskTimer(plugin, 0, delayTicks)
    }

    // ==================== Комбинированные эффекты ====================
    fun multiEffect(world: World, location: Location, configs: List<ParticleConfig>) {
        configs.forEach { spawn(world, location, it) }
    }
}
