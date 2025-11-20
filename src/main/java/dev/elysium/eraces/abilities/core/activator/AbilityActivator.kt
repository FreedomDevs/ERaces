package dev.elysium.eraces.abilities.core.activator

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.core.utils.AbilityCooldownManager
import dev.elysium.eraces.abilities.core.interfaces.IAbilityMessenger
import dev.elysium.eraces.abilities.core.registry.AbilityRegistry
import dev.elysium.eraces.abilities.core.registry.ComboRegistry
import dev.elysium.eraces.abilities.interfaces.ICooldownAbility
import dev.elysium.eraces.abilities.interfaces.IManaCostAbility
import dev.elysium.eraces.exceptions.ExceptionProcessor
import dev.elysium.eraces.exceptions.base.PlayerException
import dev.elysium.eraces.exceptions.internal.AbilityActivationException
import dev.elysium.eraces.exceptions.player.PlayerAbilityNotFoundException
import dev.elysium.eraces.exceptions.player.PlayerAbilityNotOwnedException
import dev.elysium.eraces.exceptions.player.PlayerAbilityOnCooldownException
import dev.elysium.eraces.exceptions.player.PlayerRaceNotSelectedException
import org.bukkit.entity.Player

class AbilityActivator(
    private val registry: AbilityRegistry,
    private val combos: ComboRegistry,
    private val messenger: IAbilityMessenger
) {
    fun activate(player: Player, id: String) {
        val context = ERaces.Companion.getInstance().context
        val ability = registry.get(id)
        val race = context.playerDataManager.getPlayerRace(player)

        if (ability == null)
            throw PlayerAbilityNotFoundException(player, id)

        if (race == null)
            throw PlayerRaceNotSelectedException(player)

        if (id !in race.abilities)
            throw PlayerAbilityNotOwnedException(player, id)

        if (ability is IManaCostAbility) {
            val cost = ability.getManaCost()
            if (cost > 0) {
                val manaManager = context.manaManager
                val current = manaManager.getMana(player)

                if (current < cost) {
                    messenger.send(player,"<red>Недостаточно маны! Нужно <yellow>$cost<red>, у тебя <yellow>$current")
                    return
                }

                manaManager.useMana(player, cost)
            }
        }

        if (ability is ICooldownAbility &&
            AbilityCooldownManager.hasCooldown(player, id)
        ) {
            val remaining = AbilityCooldownManager.getRemaining(player, id)
            throw PlayerAbilityOnCooldownException(player, id, remaining)
        }

        try {
            if (ability is ICooldownAbility) {
                val seconds = ability.getCooldown()
                if (seconds > 0) {
                    AbilityCooldownManager.setCooldown(player, id, seconds)
                }
            }

            ability.activate(player)

        } catch (e: PlayerException) {
            e.handle()
        } catch (t: Throwable) {
            ExceptionProcessor.process(t, player)
            AbilityActivationException(
                "Ошибка при активации способности '$id' у игрока ${player.name}",
                t,
                player
            ).handle()
        }
    }

    fun activateByCombo(player: Player, combo: String) {
        val abilityId = combos.getAbilityId(combo)

        if (abilityId == null) {
            messenger.send(player,"<red>Нет способности, назначенной на комбинацию <yellow>$combo")
            return
        }

        activate(player, abilityId)
    }
}