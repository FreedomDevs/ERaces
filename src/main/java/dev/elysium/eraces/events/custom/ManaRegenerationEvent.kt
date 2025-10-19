package dev.elysium.eraces.events.custom

import org.bukkit.OfflinePlayer
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class ManaRegenerationEvent(
    val player: OfflinePlayer,
    var regenAmount: Double
) : Event() {

    private var cancelled = false

    fun setCancelled(cancel: Boolean) {
        cancelled = cancel
    }

    fun isCancelled(): Boolean = cancelled

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList = handlerList
}
