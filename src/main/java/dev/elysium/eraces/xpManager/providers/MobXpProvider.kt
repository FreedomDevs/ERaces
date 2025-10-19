package dev.elysium.eraces.xpManager.providers

import dev.elysium.eraces.xpManager.MobXpConfig
import org.bukkit.event.entity.EntityDeathEvent

class MobXpProvider(private val config: MobXpConfig) : XpProvider<EntityDeathEvent> {
    override fun getXp(target: EntityDeathEvent): Long? {
        val mobName = target.entity.type.name.lowercase()
        return config.xpMap[mobName] ?: config.defaultXp
    }
}