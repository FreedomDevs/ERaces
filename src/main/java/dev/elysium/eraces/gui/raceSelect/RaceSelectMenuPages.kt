package dev.elysium.eraces.gui.raceSelect

import org.bukkit.Material

object RaceSelectMenuPages {

    val pages = mutableListOf<RacePage>()

    fun registerDefaults() {
        if (pages.isNotEmpty()) return

        pages += RacePage(
            id = "elf",
            displayName = "Эльф",
            lore = listOf("Ловкий и быстрый", "Дружит с природой"),
            material = Material.OAK_SAPLING,
            title = "race.elf"
        )

        pages += RacePage(
            id = "asir",
            displayName = "Асир",
            lore = listOf("Северный народ", "Закалённый холодом"),
            material = Material.DIAMOND_AXE,
            title = "race.asir"
        )

        pages += RacePage(
            id = "ork",
            displayName = "Орк",
            lore = listOf("Сильный воин", "Ненавидит эльфов"),
            material = Material.IRON_AXE,
            title = "race.orc"
        )

        pages += RacePage(
            id = "human",
            displayName = "Человек",
            lore = listOf("Гибкий", "Умелый дипломат"),
            material = Material.PLAYER_HEAD,
            title = "race.human"
        )

        pages += RacePage(
            id = "demon",
            displayName = "Зверолюд",
            lore = listOf("Могущественный", "Пушистик"),
            material = Material.BLAZE_POWDER,
            title = "race.demon"
        )
    }

    fun getById(id: String): RacePage? = pages.find { it.id == id }
}
