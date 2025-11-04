package dev.elysium.eraces.placeholders

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.datatypes.Race
import dev.elysium.eraces.placeholders.utils.PropertyCache
import dev.elysium.eraces.placeholders.utils.RaceAliases
import dev.elysium.eraces.placeholders.utils.ReflectionUtils
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer

class RaceExpansion(private val plugin: ERaces) : PlaceholderExpansion() {
    override fun getIdentifier(): String = "eraces"
    override fun getAuthor(): String = "ElysiumDev"
    override fun getVersion(): String = plugin.pluginMeta.version

    private val aliases = RaceAliases.map

    override fun onRequest(player: OfflinePlayer?, identifier: String): String? {
        val bukkitPlayer = player?.player ?: return null
        val race = plugin.context.playerDataManager.getPlayerRace(bukkitPlayer) ?: return null

        if (identifier.equals("race", true) || identifier.equals("id", true))
            return race.id ?: "unknown"

        return resolvePlaceholderValue(race, identifier)
    }

    private fun resolvePlaceholderValue(race: Race, identifier: String): String? {
        val parts = identifier.split("_").filter { it.isNotBlank() }
        if (parts.isEmpty() || !parts.first().equals("race", ignoreCase = true)) return null

        val chain = PropertyCache.getOrPut(identifier) {
            ReflectionUtils.buildPropertyChain(Race::class, parts.drop(1), aliases) ?: emptyList()
        }

        if (chain.isEmpty()) return "N/A"

        var current: Any = race
        return try {
            for (prop in chain) {
                val next = prop.get(current) ?: return "N/A"
                current = next
            }
            ReflectionUtils.formatValue(current)
        } catch (e: Exception) {
            plugin.logger.warning("Ошибка при обработке плейсхолдера '$identifier': ${e.message}")
            "N/A"
        }
    }
}