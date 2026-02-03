package dev.elysium.eraces.items.weapon.services

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.ItemResolver
import dev.elysium.eraces.items.weapon.Weapon
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class WeaponTickService private constructor(private val plugin: ERaces) {
    private var tick = 0

    init {
        object : BukkitRunnable() {
            override fun run() {
                tick++

                for (player in Bukkit.getOnlinePlayers()) {
                    handlePlayer(player)
                }
            }
        }.runTaskTimer(plugin, 1L, 1L)
    }

    private fun handlePlayer(player: Player) {
        val inv = player.inventory

        val stacks = listOf(
            inv.itemInMainHand,
            inv.itemInOffHand
        )

        for (stack in stacks) {
            val weapon = ItemResolver.resolve(stack) as? Weapon ?: continue
            handleWeaponTick(player, weapon)
        }
    }

    private fun handleWeaponTick(player: Player, weapon: Weapon) {
        val interval = weapon.tickConfig.interval.coerceAtLeast(1)
        if (tick % interval != 0) return

        weapon.onTick(player)
    }

    companion object {
        @Volatile
        private var instance: WeaponTickService? = null

        /**
         * Инициализация сервиса. Можно вызвать только один раз.
         * @throws IllegalStateException если сервис уже был инициализирован.
         */
        fun init(plugin: ERaces): WeaponTickService {
            synchronized(this) {
                if (instance != null) {
                    throw IllegalStateException("WeaponTickService уже инициализирован!")
                }
                instance = WeaponTickService(plugin)
                return instance!!
            }
        }

        /**
         * Получение уже инициализированного экземпляра.
         * @throws IllegalStateException если сервис ещё не был инициализирован.
         */
        fun get(): WeaponTickService {
            return instance ?: throw IllegalStateException("WeaponTickService ещё не инициализирован. Вызовите init() сначала.")
        }
    }
}