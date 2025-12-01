package dev.elysium.eraces.abilities.core.activator

import dev.elysium.eraces.abilities.core.interfaces.IAbilityMessenger
import dev.elysium.eraces.abilities.core.interfaces.services.activator.IAbilityComboService
import dev.elysium.eraces.abilities.core.interfaces.services.activator.IAbilityCooldownService
import dev.elysium.eraces.abilities.core.interfaces.services.activator.IAbilityManaService
import dev.elysium.eraces.abilities.core.interfaces.services.activator.IAbilityValidationService
import dev.elysium.eraces.abilities.core.registry.AbilityRegistry
import dev.elysium.eraces.abilities.interfaces.ICooldownAbility
import dev.elysium.eraces.abilities.interfaces.IManaCostAbility
import dev.elysium.eraces.exceptions.ExceptionProcessor
import dev.elysium.eraces.exceptions.base.PlayerException
import dev.elysium.eraces.exceptions.internal.AbilityActivationException
import org.bukkit.entity.Player

class AbilityActivator(
    private val registry: AbilityRegistry,
    private val messenger: IAbilityMessenger,
    private val validationService: IAbilityValidationService,
    private val manaService: IAbilityManaService,
    private val cooldownService: IAbilityCooldownService,
    private val comboService: IAbilityComboService
) {
    fun activate(player: Player, abilityId: String) {
        val ability = registry.get(abilityId)
            ?: throw AbilityActivationException("Способность '$abilityId' не найдена", null, player)

        player.runAbilityAction {
            validationService.validateAbilityAccess(player, abilityId)

            (ability as? IManaCostAbility)?.let {
                manaService.checkAndConsume(player, it)
            }

            (ability as? ICooldownAbility)?.let {
                cooldownService.check(player, abilityId, it)
            }

            (ability as? ICooldownAbility)?.let {
                cooldownService.apply(player, abilityId, it)
            }

            ability.activate(player)
        }
    }

    fun activateByCombo(player: Player, combo: String) {
        comboService.resolve(combo)?.let {
            activate(player, it)
        } ?: messenger.send(
            player,
            "<red>Нет способности, назначенной на комбинацию <yellow>$combo"
        )
    }

    private inline fun Player.runAbilityAction(block: () -> Unit) {
        try {
            block()
        } catch (e: PlayerException) {
            e.handle()
        } catch (t: Throwable) {
            ExceptionProcessor.process(t, this)
            AbilityActivationException(
                "Ошибка при активации способности у игрока $name",
                t,
                this
            ).handle()
        }
    }
}
