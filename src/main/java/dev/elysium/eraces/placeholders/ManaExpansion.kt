package dev.elysium.eraces.placeholders

import dev.elysium.eraces.ERaces
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer

class ManaExpansion(private val plugin: ERaces): PlaceholderExpansion() {
    override fun getIdentifier(): String = "eraces"
    override fun getAuthor(): String = "ElysiumDev"
    override fun getVersion(): String = plugin.description.version

    override fun onRequest(player: OfflinePlayer?, identifier: String): String? {
        if (player == null) return null

        val manaManager = plugin.context.manaManager ?: return null

        return when (identifier.lowercase()) {
            "mana" -> String.format("%.1f", manaManager.getMana(player))
            "maxmana" -> String.format("%.1f", manaManager.getMaxMana(player))
            "manapct" -> {
                val max = manaManager.getMaxMana(player)
                if (max <= 0) "0"
                else String.format("%.0f", (manaManager.getMana(player) / max) * 100)
            }
            else -> null
        }
    }

}