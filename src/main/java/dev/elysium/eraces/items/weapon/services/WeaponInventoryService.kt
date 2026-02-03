package dev.elysium.eraces.items.weapon.services

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.SlotType
import dev.elysium.eraces.items.core.ItemResolver
import dev.elysium.eraces.items.weapon.Weapon
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import kotlin.collections.iterator

class WeaponInventoryService private constructor()
 : BukkitRunnable() {

    override fun run() {
        val map = mutableMapOf<Weapon, MutableMap<Player, MutableSet<SlotType>>>()

        for (player in Bukkit.getOnlinePlayers()) {
            for ((index, item) in player.inventory.contents.withIndex()) {
                if (item == null || item.type.isAir) continue

                val weapon = ItemResolver.resolve(item) as? Weapon ?: continue

                val slotType = when {
                    item == player.inventory.itemInMainHand -> SlotType.MAINHAND
                    index < 9 -> SlotType.HOTBAR
                    else -> SlotType.INVENTORY
                }

                map.computeIfAbsent(weapon) { mutableMapOf() }
                    .computeIfAbsent(player) { mutableSetOf() }
                    .add(slotType)
            }

            val offhandItem = player.inventory.itemInOffHand
            if (!offhandItem.type.isAir) {
                val weapon = ItemResolver.resolve(offhandItem) as? Weapon
                weapon?.let {
                    map.computeIfAbsent(it) { mutableMapOf() }
                        .computeIfAbsent(player) { mutableSetOf() }
                        .add(SlotType.OFFHAND)
                }
            }
        }


        for ((weapon, playersMap) in map) {
            if (playersMap.isNotEmpty()) {
                weapon.onInventory(playersMap)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: WeaponInventoryService? = null

        /**
         * Инициализация сервиса. Можно вызвать только один раз.
         * @throws IllegalStateException если сервис уже был инициализирован.
         */
        fun init(plugin: ERaces, period: Long = 5L): WeaponInventoryService {
            synchronized(this) {
                if (instance != null) {
                    throw IllegalStateException("WeaponInventoryService уже инициализирован!")
                }
                val service = WeaponInventoryService()
                service.runTaskTimer(plugin, 0L, period)
                instance = service
                return service
            }
        }

        /**
         * Получение уже инициализированного экземпляра.
         * @throws IllegalStateException если сервис ещё не был инициализирован.
         */
        fun get(): WeaponInventoryService {
            return instance ?: throw IllegalStateException("WeaponInventoryService ещё не инициализирован. Вызовите init() сначала.")
        }
    }

}