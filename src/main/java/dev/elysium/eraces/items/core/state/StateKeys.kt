package dev.elysium.eraces.items.core.state

import dev.elysium.eraces.ERaces.Companion.PLUGIN_NAMESPACE
import org.bukkit.NamespacedKey

object StateKeys {
    val HITS = NamespacedKey(PLUGIN_NAMESPACE, "hits")
    val DURABILITY =  NamespacedKey(PLUGIN_NAMESPACE, "max_hits")
    val KD = NamespacedKey(PLUGIN_NAMESPACE, "kd")

    val MANA = NamespacedKey(PLUGIN_NAMESPACE, "mana")

    val IS_ABILITY_CHARGED = NamespacedKey(PLUGIN_NAMESPACE, "is_ability_charged")
    val CHARGE_START = NamespacedKey(PLUGIN_NAMESPACE, "charge_start")
    val CHARGE_TASK_ID = NamespacedKey(PLUGIN_NAMESPACE, "charge_task_id")

    val ABILITY_CHARGE_START =
        NamespacedKey(PLUGIN_NAMESPACE, "ability_charge_start")
    val ABILITY_CHARGED_UNTIL  =
        NamespacedKey(PLUGIN_NAMESPACE, "ability_charged_until")

    val ARROWS_QUIVER = NamespacedKey(PLUGIN_NAMESPACE, "arrows_quiver")
}