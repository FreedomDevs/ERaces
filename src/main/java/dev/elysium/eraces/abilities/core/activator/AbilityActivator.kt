package dev.elysium.eraces.abilities.core.activator

import dev.elysium.eraces.abilities.core.interfaces.IAbilityMessenger
import dev.elysium.eraces.abilities.core.interfaces.services.activator.*
import dev.elysium.eraces.abilities.core.registry.AbilityRegistry
import dev.elysium.eraces.abilities.interfaces.*
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

        try {
            validationService.validateAbilityAccess(player, abilityId)

            if (ability is IManaCostAbility)
                manaService.checkAndConsume(player, ability)

            if (ability is ICooldownAbility)
                cooldownService.check(player, abilityId, ability)

            ability.activate(player)

            if (ability is ICooldownAbility)
                cooldownService.apply(player, abilityId, ability)

        } catch (e: PlayerException) {
            e.handle()
        } catch (t: Throwable) {
            ExceptionProcessor.process(t, player)
            AbilityActivationException(
                "Ошибка при активации способности '$abilityId' у игрока ${player.name}",
                t,
                player
            ).handle()
        }
    }

    fun activateByCombo(player: Player, combo: String) {
        val abilityId = comboService.resolve(combo)

        if (abilityId == null) {
            messenger.send(player,"<red>Нет способности, назначенной на комбинацию <yellow>$combo")
            return
        }

        activate(player, abilityId)
    }
}