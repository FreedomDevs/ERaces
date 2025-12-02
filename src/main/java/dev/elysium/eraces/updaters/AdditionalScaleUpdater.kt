package dev.elysium.eraces.updaters

import dev.elysium.eraces.datatypes.Race
import dev.elysium.eraces.updaters.base.IUpdater
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player

class AdditionalScaleUpdater : IUpdater {

    val key = NamespacedKey("eraces", "additional_scale")
    override fun update(race: Race, player: Player) {
        val bonus = race.additionalScale

        val attribute = player.getAttribute(Attribute.SCALE) ?: return

        attribute.addModifier(AttributeModifier(key, bonus, AttributeModifier.Operation.ADD_NUMBER))
    }
}