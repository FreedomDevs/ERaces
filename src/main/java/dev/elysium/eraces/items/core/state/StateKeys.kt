package dev.elysium.eraces.items.core.state

import dev.elysium.eraces.ERaces
import org.bukkit.NamespacedKey

object StateKeys {
    lateinit var HITS: NamespacedKey
    lateinit var DURABILITY: NamespacedKey
    lateinit var KD: NamespacedKey

    lateinit var MANA: NamespacedKey

    lateinit var IS_ABILITY_CHARGED: NamespacedKey
    lateinit var CHARGE_START: NamespacedKey
    lateinit var CHARGE_TASK_ID: NamespacedKey

    lateinit var ABILITY_CHARGE_START: NamespacedKey
    lateinit var ABILITY_CHARGED_UNTIL: NamespacedKey

    lateinit var ARROWS_КОЛЧАН: NamespacedKey

    fun init(plugin: ERaces) {
        HITS = NamespacedKey(plugin, "hits")
        DURABILITY = NamespacedKey(plugin, "max_hits")
        KD = NamespacedKey(plugin, "kd")

        MANA = NamespacedKey(plugin, "mana")

        IS_ABILITY_CHARGED = NamespacedKey(plugin, "is_ability_charged")
        CHARGE_START = NamespacedKey(plugin, "charge_start")
        CHARGE_TASK_ID = NamespacedKey(plugin, "charge_task_id")

        ABILITY_CHARGE_START =
            NamespacedKey(plugin, "ability_charge_start")

        ABILITY_CHARGED_UNTIL =
            NamespacedKey(plugin, "ability_charged_until")

        ARROWS_КОЛЧАН = NamespacedKey(plugin, "arrows_колчан")
    }
}