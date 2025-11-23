package dev.elysium.eraces.abilities.core.interfaces.providers

import org.bukkit.entity.Player

interface IManaProvider {
    fun getMana(player: Player): Double
    fun useMana(player: Player, amount: Double)
}