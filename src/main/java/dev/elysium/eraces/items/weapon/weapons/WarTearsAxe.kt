package dev.elysium.eraces.items.weapon.weapons

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.items.core.state.ItemState
import dev.elysium.eraces.items.core.state.StateKeys
import dev.elysium.eraces.items.weapon.MeleeWeapon
import dev.elysium.eraces.utils.actionMsg
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.EquipmentSlot

class WarTearsAxe(
    override val plugin: ERaces
) : MeleeWeapon(

    id = "war_tears_axe",

    material = Material.IRON_AXE,

    name = "<red>Слезы Войны",

    damage = 16.0,
    attackSpeed = 0.8,

    isUnbreakable = false,
    maxDurability = 2400,

    options = mapOf(
        "critChance" to 0.1,

        "lore" to listOf(
            "Двусторонний топор Слез Войны",
            "",
            "<gray>Урон: <red>16",
            "<gray>Скорость атаки: <yellow>0.8",
            "<gray>Прочность: <green>2400",
            "",
            "<gold>Способность: Удар Кратоса",
            "<gray>Заряжается ~2 секунды",
            "<gray>Следующий удар наносит <red>25 урона",
            "<gray>Пробивает щиты",
            "<gray>x1.4 урона по боссам"
        )
    )
) {

    companion object {
        private const val ABILITY_DAMAGE = 25.0

        private const val CHARGE_TIME = 2000L
        private const val CHARGED_DURATION = 5000L

        private const val SHIELD_COOLDOWN_TICKS = 100
    }

    override fun onInteract(
        player: Player,
        hand: EquipmentSlot,
        click: ClickType
    ) {

        if (click != ClickType.RIGHT) return
        if (hand != EquipmentSlot.HAND) return

        val stack = player.inventory.itemInMainHand

        // защита от бага со сменой предмета
        if (stack.type != Material.IRON_AXE) return

        val state = ItemState(stack)

        val now = System.currentTimeMillis()

        val chargedUntil =
            state.getLong(StateKeys.ABILITY_CHARGED_UNTIL)

        // Уже заряжен
        if (chargedUntil > now) {
            player.actionMsg(
                "<red>Удар Кратоса уже заряжен!"
            )
            return
        }

        val chargeStart =
            state.getLong(StateKeys.ABILITY_CHARGE_START)

        val isCharging =
            now - chargeStart < CHARGE_TIME

        // Уже идет зарядка
        if (isCharging) {
            player.actionMsg(
                "<yellow>Топор уже заряжается..."
            )
            return
        }

        // Начало зарядки
        state.setLong(
            StateKeys.ABILITY_CHARGE_START,
            now
        )

        player.actionMsg(
            "<gold>Вы начинаете заряжать Удар Кратоса..."
        )

        // Через 2 секунды завершаем заряд
        plugin.server.scheduler.runTaskLater(
            plugin,
            Runnable {

                // Игрок мог сменить предмет
                val current =
                    player.inventory.itemInMainHand

                if (current.type != Material.IRON_AXE)
                    return@Runnable

                val currentState =
                    ItemState(current)

                val currentChargeStart =
                    currentState.getLong(
                        StateKeys.ABILITY_CHARGE_START
                    )

                // Заряд был сброшен/перезапущен
                if (currentChargeStart != now)
                    return@Runnable

                currentState.setLong(
                    StateKeys.ABILITY_CHARGED_UNTIL,
                    System.currentTimeMillis() + CHARGED_DURATION
                )

                player.actionMsg(
                    "<green>Удар Кратоса заряжен!"
                )

            },
            40L // 2 секунды
        )
    }

    override fun onHit(
        event: EntityDamageByEntityEvent
    ) {

        super.onHit(event)

        val player =
            event.damager as? Player ?: return

        val target =
            event.entity as? LivingEntity ?: return

        val stack =
            player.inventory.itemInMainHand

        if (stack.type != Material.IRON_AXE)
            return

        val state = ItemState(stack)

        val now = System.currentTimeMillis()

        val chargedUntil =
            state.getLong(StateKeys.ABILITY_CHARGED_UNTIL)

        // Заряд неактивен
        if (chargedUntil < now)
            return

        // Сбрасываем заряд
        state.setLong(
            StateKeys.ABILITY_CHARGED_UNTIL,
            0L
        )

        state.setLong(
            StateKeys.ABILITY_CHARGE_START,
            0L
        )

        // Множитель по боссам
        val multiplier =
            if (target.isCustomBoss()) 1.4
            else 1.0

        event.damage =
            ABILITY_DAMAGE * multiplier

        // Пробитие щита
        if (target is Player) {

            target.setCooldown(
                Material.SHIELD,
                SHIELD_COOLDOWN_TICKS
            )

            target.world.playSound(
                target.location,
                "item.shield.break",
                1f,
                1f
            )
        }

        player.actionMsg(
            "<green>Удар Кратоса активирован!"
        )
    }

    fun LivingEntity.isCustomBoss(): Boolean {

        val name = customName ?: return false

        return name.contains(
            "Босс",
            ignoreCase = true
        )
    }
}