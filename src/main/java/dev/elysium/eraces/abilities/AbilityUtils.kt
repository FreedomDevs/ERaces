package dev.elysium.eraces.abilities

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.utils.TimeParser
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

object AbilityUtils {

    /** Запускает однократную задачу через [delay] ("5s", "2m"). */
    fun runLater(plugin: ERaces, delay: String, block: () -> Unit): BukkitTask {
        val ticks = TimeParser.parseToTicks(delay)
        return Bukkit.getScheduler().runTaskLater(plugin, Runnable { block() }, ticks)
    }

    /** Запускает повторяющуюся задачу с интервалом [tickInterval] (в тиках, 20L = 1 сек). */
    fun runRepeating(plugin: ERaces, tickInterval: Long = 20L, block: () -> Unit): BukkitTask {
        return Bukkit.getScheduler().runTaskTimer(plugin, Runnable { block() }, 0L, tickInterval)
    }

    /**
     * Запускает повторяющуюся задачу на определённую длительность [duration],
     * вызывает [block] каждый интервал.
     */
    fun runRepeatingForDuration(
        plugin: ERaces,
        duration: String,
        tickInterval: Long = 20L,
        block: (elapsedSeconds: Long) -> Unit
    ): BukkitTask {
        val totalTicks = TimeParser.parseToTicks(duration)
        var elapsed = 0L
        var task: BukkitTask? = null

        task = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            if (elapsed >= totalTicks) {
                task?.cancel()
                return@Runnable
            }
            block(elapsed)
            elapsed += tickInterval
        }, 0L, tickInterval)
        return task
    }

    /**
     * Регистрирует временного слушателя, который автоматически снимается через [duration].
     */
    fun registerTemporaryListener(plugin: ERaces, listener: Listener, duration: String) {
        Bukkit.getPluginManager().registerEvents(listener, plugin)
        val ticks = TimeParser.parseToTicks(duration)
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            HandlerList.unregisterAll(listener)
        }, ticks)
    }

    /** Запускает задачу асинхронно. */
    fun runAsync(plugin: ERaces, block: () -> Unit): BukkitTask {
        return Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable { block() })
    }

    /** Запускает асинхронную повторяющуюся задачу. */
    fun runAsyncRepeating(plugin: ERaces, delay: Long, period: Long, block: () -> Unit): BukkitTask {
        return object : BukkitRunnable() {
            override fun run() = block()
        }.runTaskTimerAsynchronously(plugin, delay, period)
    }
}
