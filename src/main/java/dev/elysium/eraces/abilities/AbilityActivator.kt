package dev.elysium.eraces.abilities

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.interfaces.IAbility
import dev.elysium.eraces.abilities.interfaces.IComboActivatable
import dev.elysium.eraces.abilities.interfaces.ICooldownAbility
import dev.elysium.eraces.abilities.interfaces.IManaCostAbility
import dev.elysium.eraces.exceptions.ExceptionProcessor
import dev.elysium.eraces.exceptions.base.PlayerException
import dev.elysium.eraces.exceptions.internal.AbilityActivationException
import dev.elysium.eraces.exceptions.player.PlayerAbilityNotFoundException
import dev.elysium.eraces.exceptions.player.PlayerAbilityNotOwnedException
import dev.elysium.eraces.exceptions.player.PlayerAbilityOnCooldownException
import dev.elysium.eraces.exceptions.player.PlayerRaceNotSelectedException
import dev.elysium.eraces.utils.actionMsg
import org.bukkit.entity.Player

class AbilityActivator(
    private val abilities: Map<String, IAbility>
) {
    fun activate(player: Player, id: String) {
        val context = ERaces.getInstance().context
        val ability = abilities[id]
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
                    player.actionMsg("<red>Недостаточно маны! Нужно <yellow>$cost<red>, у тебя <yellow>$current")
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
        val ability = abilities.values
            .filterIsInstance<IComboActivatable>()
            .firstOrNull { it.getComboKey() == combo }

        if (ability == null) {
            player.actionMsg("<red>Нет способности, назначенной на комбинацию <yellow>$combo")
            return
        }

        activate(player, (ability as IAbility).id)
    }
}