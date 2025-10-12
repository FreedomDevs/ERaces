package dev.elysium.eraces.gui

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.utils.ChatUtil
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class RaceSelectMenu(private val player: Player) {
    companion object {
        private val pages = mutableListOf<RacePage>()

        fun registerDefaults() {
            if (pages.isNotEmpty()) return // уже инициализировано

            pages.add(
                RacePage(
                    "elf",
                    "Эльф",
                    listOf("Ловкий и быстрый"),
                    Material.OAK_SAPLING
                )
            )
            pages.add(
                RacePage(
                    "orc",
                    "Орк",
                    listOf("Сильный воин", "Туповат"),
                    Material.IRON_SWORD
                )
            )
            pages.add(
                RacePage(
                    "human",
                    "Человек",
                    listOf("Лох"),
                    Material.PLAYER_HEAD
                )
            )
            pages.add(
                RacePage(
                    "demon",
                    "Зверолюд",
                    listOf("Могущественный", "Пуфыстик"),
                    Material.BLAZE_POWDER
                )
            )
        }
    }

    private var currentPageIndex = 0
    private lateinit var inv: Inventory

    fun open() {
        createInventory()
        player.openInventory(inv)
    }

    private fun createInventory() {
        val racePage = pages[currentPageIndex]
        inv = Bukkit.createInventory(null, 54, "Выбор расы [${racePage.displayName}]")

        inv.setItem(22, racePage.toItem())

        inv.setItem(31, createButton(Material.EMERALD_BLOCK, "§aВыбрать расу"))

        if (currentPageIndex > 0)
            inv.setItem(45, createButton(Material.ARROW, "§eПредыдущая раса"))
        if (currentPageIndex < pages.size - 1)
            inv.setItem(53, createButton(Material.ARROW, "§eСледующая раса"))
    }

    private fun createButton(mat: Material, name: String): ItemStack {
        val item = ItemStack(mat)
        val meta = item.itemMeta!!
        meta.setDisplayName(name)
        item.itemMeta = meta
        return item
    }

    fun handleClick(e: InventoryClickEvent) {
        e.isCancelled = true
        val item = e.currentItem ?: return
        val name = item.itemMeta?.displayName ?: return

        when {
            name.contains("Предыдущая") -> {
                currentPageIndex--
                open()
            }

            name.contains("Следующая") -> {
                currentPageIndex++
                open()
            }

            name.contains("Выбрать") -> {
                val race = pages[currentPageIndex]
                player.closeInventory()
                ChatUtil.sendAction(player, "<green>Ты выбрал расу: <gold>${race.displayName}")

                ERaces.getPlayerMng().setPlayerRace(player, race.id)
                dev.elysium.eraces.RacesReloader.reloadRaceForPlayer(player)
            }
        }
    }
}
