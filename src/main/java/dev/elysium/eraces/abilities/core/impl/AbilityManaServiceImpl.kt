package dev.elysium.eraces.abilities.core.impl

import dev.elysium.eraces.abilities.core.interfaces.IAbilityMessenger
import dev.elysium.eraces.abilities.core.interfaces.providers.IManaProvider
import dev.elysium.eraces.abilities.core.interfaces.services.activator.IAbilityManaService
import dev.elysium.eraces.abilities.interfaces.IManaCostAbility
import dev.elysium.eraces.exceptions.player.NotEnoughManaException
import org.bukkit.entity.Player

class AbilityManaServiceImpl(
    private val manaProvider: IManaProvider,
    private val messenger: IAbilityMessenger
) : IAbilityManaService {
    override fun checkAndConsume(player: Player, ability: IManaCostAbility) {
        val cost = ability.getManaCost()
        if (cost <= 0) return

        val current = manaProvider.getMana(player)

        if (current < cost) {
            messenger.send(
                player,
                "<red>Недостаточно маны! Нужно <yellow>$cost<red>, у тебя <yellow>$current"
            )
            throw NotEnoughManaException(
                player,
                "Недостаточно маны! Нужно $cost, а у тебя $current"
            )
        }

        manaProvider.useMana(player, cost)
    }
}