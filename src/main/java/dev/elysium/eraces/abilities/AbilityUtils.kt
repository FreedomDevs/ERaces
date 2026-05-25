package dev.elysium.eraces.abilities

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.utils.TimeParser
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

/**
 * Утилитарный объект для работы с задачами и слушателями, связанными со способностями.
 *
 * Содержит функции для:
 * - однократного и повторяющегося запуска задач,
 * - выполнения задач на определённую длительность,
 * - регистрации временных слушателей,
 * - асинхронного выполнения задач.
 */
object AbilityUtils {

    /**
     * Запускает однократную задачу через заданный [delay].
     *
     * @param plugin основной плагин [ERaces]
     * @param delay задержка в формате строки, например "5s", "2m"
     * @param block блок кода, который будет выполнен
     * @return [BukkitTask] задача, которую можно отменить
     */
    fun runLater(plugin: ERaces, delay: String, block: () -> Unit): BukkitTask {
        val ticks = TimeParser.parseToTicks(delay)
        return Bukkit.getScheduler().runTaskLater(plugin, Runnable { block() }, ticks)
    }

    /**
     * Запускает повторяющуюся задачу с интервалом [tickInterval].
     *
     * @param plugin основной плагин [ERaces]
     * @param tickInterval интервал между вызовами в тиках (20L = 1 секунда)
     * @param block блок кода, который будет выполняться на каждом интервале
     * @return [BukkitTask] задача, которую можно отменить
     */
    fun runRepeating(plugin: ERaces, tickInterval: Long = 20L, block: () -> Unit): BukkitTask {
        return Bukkit.getScheduler().runTaskTimer(plugin, Runnable { block() }, 0L, tickInterval)
    }

    /**
     * Запускает повторяющуюся задачу на определённую длительность [duration].
     * Вызывает [block] каждый интервал, передавая прошедшее время в секундах.
     *
     * @param plugin основной плагин [ERaces]
     * @param duration общая длительность задачи в формате строки, например "10s", "1m"
     * @param tickInterval интервал между вызовами в тиках
     * @param block блок кода с параметром elapsedSeconds — прошедшее время
     * @return [BukkitTask] задача, которую можно отменить
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
     * Регистрирует временного слушателя [listener], который автоматически снимается через [duration].
     *
     * @param plugin основной плагин [ERaces]
     * @param listener слушатель событий
     * @param duration длительность активности слушателя в формате строки, например "5s"
     */
    fun registerTemporaryListener(plugin: ERaces, listener: Listener, duration: String) {
        Bukkit.getPluginManager().registerEvents(listener, plugin)
        val ticks = TimeParser.parseToTicks(duration)
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            HandlerList.unregisterAll(listener)
        }, ticks)
    }

    /**
     * Запускает задачу асинхронно.
     *
     * @param plugin основной плагин [ERaces]
     * @param block блок кода, который будет выполнен
     * @return [BukkitTask] задача, которую можно отменить
     */
    fun runAsync(plugin: ERaces, block: () -> Unit): BukkitTask {
        return Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable { block() })
    }

    /**
     * Запускает асинхронную повторяющуюся задачу с заданной задержкой [delay] и периодом [period].
     *
     * @param plugin основной плагин [ERaces]
     * @param delay задержка перед первым запуском в тиках
     * @param period период между вызовами в тиках
     * @param block блок кода, который будет выполняться на каждом интервале
     * @return [BukkitTask] задача, которую можно отменить
     */
    fun runAsyncRepeating(plugin: ERaces, delay: Long, period: Long, block: () -> Unit): BukkitTask {
        return object : BukkitRunnable() {
            override fun run() = block()
        }.runTaskTimerAsynchronously(plugin, delay, period)
    }
}
