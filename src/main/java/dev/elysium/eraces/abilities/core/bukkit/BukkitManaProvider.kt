package dev.elysium.eraces.abilities.core.bukkit

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.core.interfaces.providers.IManaProvider
import org.bukkit.entity.Player

class BukkitManaProvider : IManaProvider {
    private val plugin: ERaces = ERaces.getInstance()

    override fun getMana(player: Player): Double {
        return plugin.context.manaManager.getMana(player)
    }

    override fun useMana(player: Player, amount: Double) {
        plugin.context.manaManager.useMana(player, amount)
    }
}