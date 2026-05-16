package dev.elysium.eraces.items.колчаны.колчаны

import dev.elysium.eraces.items.колчаны.Колчаны
import dev.elysium.eraces.utils.ChatUtil
import io.papermc.paper.datacomponent.item.ItemLore
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Suppress("UnstableApiUsage")
class BeerКолчан : Колчаны(
    "beer_колчан",
    "<green>Пивной колчан",
    ItemLore.lore().addLine(ChatUtil.parse("<red>Test1234")).build(),
    4,
    1.0

) {
    override fun onHit(event: EntityDamageByEntityEvent) {
        super.onHit(event)

        val entity = event.entity as? LivingEntity ?: return
        entity.addPotionEffect(PotionEffect(PotionEffectType.POISON, 3 * 20, 0))
    }
}