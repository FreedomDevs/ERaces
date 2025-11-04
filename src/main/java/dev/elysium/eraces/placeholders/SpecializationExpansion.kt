package dev.elysium.eraces.placeholders

import dev.elysium.eraces.ERaces
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer

class SpecializationExpansion(private val plugin: ERaces) : PlaceholderExpansion() {
    override fun getIdentifier(): String = "eraces_spec"
    override fun getAuthor(): String = "ElysiumDev"
    override fun getVersion(): String = plugin.pluginMeta.version

    override fun onRequest(player: OfflinePlayer?, identifier: String): String? {
        if (player == null) return null

        val specManager = plugin.context.specializationsManager ?: return null
        val playerData = plugin.context.specializationsManager
            .specPlayersData[player.uniqueId] ?: return "no_data"

        return when (identifier.lowercase()) {
            "name" -> playerData.specialization.ifBlank { "none" }
            "level" -> playerData.level.toString()

            // Текущий XP игрока
            "xp" -> playerData.xp.toString()

            // XP, нужный для апа
            "xp_next" -> specManager.config.getXpNext(playerData.level).toString()

            // Сколько осталось до апа
            "xp_to_next" -> {
                val need = specManager.config.getXpNext(playerData.level)
                (need - playerData.xp).coerceAtLeast(0).toString()
            }

            // XP прогресс
            "xp_percent" -> {
                val need = specManager.config.getXpNext(playerData.level)
                if (need <= 0) "0"
                else String.format("%.1f", (playerData.xp.toDouble() / need) * 100)
            }

            else -> null
        }
    }
}
