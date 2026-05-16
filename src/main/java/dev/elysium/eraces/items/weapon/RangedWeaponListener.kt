package dev.elysium.eraces.items.weapon

import dev.elysium.eraces.items.core.ItemResolver
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent

class RangedWeaponListener : Listener {

    @EventHandler
    fun onShoot(event: EntityShootBowEvent) {

        val player =
            event.entity as? Player ?: return

        val stack =
            event.bow ?: return

        val weapon =
            ItemResolver.resolve(stack)
                    as? RangedWeapon ?: return

        if (!weapon.consumeArrow(
                player,
                stack
            )
        ) {

            event.isCancelled = true
            return
        }

        val arrow =
            event.projectile as? Arrow ?: return

        weapon.modifyArrow(
            player,
            arrow
        )
    }
}