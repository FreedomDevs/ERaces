package dev.elysium.eraces.updaters

import dev.elysium.eraces.datatypes.Race
import dev.elysium.eraces.updaters.base.IUpdater
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player

class BaseDamageUpdater : IUpdater {

    val key = NamespacedKey("eraces", "base_damage")
    override fun update(race: Race, player: Player) {
        val bonus = race.baseDamageBonus

        val attribute = player.getAttribute(Attribute.ATTACK_DAMAGE) ?: return

        attribute.modifiers.firstOrNull { it.key == key }?.let {
            attribute.removeModifier(it)
        }

        attribute.addModifier(
            AttributeModifier(
                key,
                bonus,
                AttributeModifier.Operation.ADD_NUMBER
            )
        )
    }
}