package dev.elysium.eraces.listeners.custom

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerXpGainEvent(
    val player: Player,
    var xp: Long,
    val reason: Reason
) : Event(), Cancellable {

    var cancelled: Boolean = false
        private set

    override fun isCancelled(): Boolean = cancelled
    override fun setCancelled(cancel: Boolean) {
        cancelled = cancel
    }

    enum class Reason {
        MINING, MOB_KILL, CRAFT, PLAYER_KILL, CUSTOM
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList = handlerList
}
