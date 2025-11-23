package dev.elysium.eraces.abilities.core.impl.bukkit

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.core.interfaces.providers.IManaProvider
import org.bukkit.entity.Player

class BukkitManaProviderImpl : IManaProvider {
    private val plugin: ERaces = ERaces.getInstance()

    override fun getMana(player: Player): Double =
        plugin.context.manaManager.getMana(player)

    override fun useMana(player: Player, amount: Double) {
        plugin.context.manaManager.useMana(player, amount)
    }
}