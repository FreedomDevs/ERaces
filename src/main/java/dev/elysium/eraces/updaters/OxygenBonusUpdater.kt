package dev.elysium.eraces.updaters

import dev.elysium.eraces.datatypes.Race
import dev.elysium.eraces.updaters.base.IUpdater
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.entity.Player

class OxygenBonusUpdater : IUpdater {
    val key: NamespacedKey = NamespacedKey("eraces", "oxygen_bonus")
    override fun update(race: Race, player: Player) {
        val attr = player.getAttribute(Attribute.OXYGEN_BONUS) ?: return
        attr.removeModifier(key)

        attr.modifiers.firstOrNull { it.key == key }?.let {
            attr.removeModifier(it)
        }

        attr.addModifier(AttributeModifier(key, race.oxygenBonus, Operation.ADD_NUMBER))
    }
}