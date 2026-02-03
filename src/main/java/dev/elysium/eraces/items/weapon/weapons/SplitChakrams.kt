package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.weapon.MeleeWeapon
import org.bukkit.Material
import org.bukkit.event.entity.EntityDamageEvent

class SplitChakrams(
    override val plugin: ERaces
) : MeleeWeapon(
    id = "split_chakrams",
    material = Material.IRON_SWORD,
    name = "<pink>Разделённые Чакрам",
    damage = 5.0,
    attackSpeed = 3.3,
    maxDurability = 750,
    options = mapOf(
        "lore" to WeaponLoreBuilder.build(
            damage = 5.0,
            attackSpeed = 3.3,
            activeAbilities = listOf(
                "<gradient:#66ccff:#3366ff>Способность: Блок</gradient>" to listOf(
                    "<gray>▸ <white>Блокирует <aqua>40%</aqua> получаемого урона",
                    "<gray>▸ <green>Перезарядка отсутствует</green>"
                )
            )
        )
    )
) {
    override fun onDamage(event: EntityDamageEvent) {
        event.damage *= 0.6
    }
}