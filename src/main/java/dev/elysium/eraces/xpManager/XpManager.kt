package dev.elysium.eraces.xpManager

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.events.custom.PlayerXpGainEvent
import dev.elysium.eraces.xpManager.providers.BlockXpProvider
import dev.elysium.eraces.xpManager.providers.CraftXpProvider
import dev.elysium.eraces.xpManager.providers.MobXpProvider
import dev.elysium.eraces.xpManager.providers.PlayerKillXpProvider
import dev.elysium.eraces.xpManager.providers.XpProvider
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.CraftItemEvent

class XpManager : Listener {
    private val blockProvider: XpProvider<BlockBreakEvent> = BlockXpProvider(defaultBlockXpConfig)
    private val mobProvider: XpProvider<EntityDeathEvent> = MobXpProvider(defaultMobXpConfig)
    private val craftProvider: XpProvider<CraftItemEvent> = CraftXpProvider(defaultCraftXpConfig)
    private val playerKillProvider: XpProvider<PlayerDeathEvent> = PlayerKillXpProvider(defaultPlayerKillXpConfig)

    private fun addXp(player: Player, xp: Long, reason: PlayerXpGainEvent.Reason) {
        val event = PlayerXpGainEvent(player, xp, reason)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return

        ERaces.getInstance().context.specializationsManager.addXp(player, event.xp)
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        blockProvider.getXp(event)?.let { addXp(event.player, it, PlayerXpGainEvent.Reason.MINING) }
    }

    @EventHandler
    fun onMobKill(event: EntityDeathEvent) {
        val killer = event.entity.killer ?: return
        mobProvider.getXp(event)?.let { addXp(killer, it, PlayerXpGainEvent.Reason.MOB_KILL) }
    }

    @EventHandler
    fun onCraft(event: CraftItemEvent) {
        craftProvider.getXp(event)?.let { addXp(event.whoClicked as Player, it, PlayerXpGainEvent.Reason.CRAFT) }
    }

    @EventHandler
    fun onPlayerKill(event: PlayerDeathEvent) {
        playerKillProvider.getXp(event)?.let { killer ->
            addXp(event.entity.killer!!, killer, PlayerXpGainEvent.Reason.PLAYER_KILL)
        }
    }

    fun addCustomXp(player: Player, xp: Long) {
        addXp(player, xp, PlayerXpGainEvent.Reason.CUSTOM)
    }
}