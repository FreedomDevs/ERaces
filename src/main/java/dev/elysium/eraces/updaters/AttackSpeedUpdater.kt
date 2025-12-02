package dev.elysium.eraces.updaters

import dev.elysium.eraces.datatypes.Race
import dev.elysium.eraces.updaters.base.IUpdater
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player

class AttackSpeedUpdater : IUpdater {

    private val key = NamespacedKey("eraces", "attack_speed")

    override fun update(race: Race, player: Player) {
        val asMultiplier = race.attackSpeedMultiplier

        val attribute = player.getAttribute(Attribute.ATTACK_SPEED) ?: return
        val baseValue = attribute.baseValue

        attribute.addModifier(AttributeModifier(key, baseValue * asMultiplier, AttributeModifier.Operation.MULTIPLY_SCALAR_1))
    }
}