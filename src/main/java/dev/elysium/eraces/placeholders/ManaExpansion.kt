package dev.elysium.eraces.placeholders

import dev.elysium.eraces.ERaces
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer

class ManaExpansion(private val plugin: ERaces): PlaceholderExpansion() {
    override fun getIdentifier(): String = "eraces_mana"
    override fun getAuthor(): String = "ElysiumDev"
    override fun getVersion(): String = plugin.pluginMeta.version

    override fun onRequest(player: OfflinePlayer?, identifier: String): String? {
        if (player == null) return null

        val bukkitPlayer = player.player ?: return null
        val manaManager = plugin.context?.manaManager ?: return null

        return when (identifier.lowercase()) {
            "mana" -> format(manaManager.getMana(bukkitPlayer))
            "mana_max" -> format(manaManager.getMaxMana(bukkitPlayer))
            "mana_percent" -> {
                val max = manaManager.getMaxMana(bukkitPlayer)
                if (max <= 0) "0"
                else format((manaManager.getMana(bukkitPlayer) / max) * 100, 0)
            }
            else -> null
        }
    }

    private fun format(num: Double, decimals: Int = 1): String =
        "%.${decimals}f".format(num)
}
