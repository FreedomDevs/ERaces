package dev.elysium.eraces.xpManager.providers

import dev.elysium.eraces.xpManager.BlockXpConfig
import org.bukkit.event.block.BlockBreakEvent

class BlockXpProvider(private val config: BlockXpConfig) : XpProvider<BlockBreakEvent> {
    override fun getXp(target: BlockBreakEvent): Long? {
        return config.xpMap[target.block.type]
    }
}