package dev.elysium.eraces.commands

import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.elysium.eraces.items.core.ItemRegistry
import dev.elysium.eraces.items.core.ItemType
import org.bukkit.Bukkit

object CommandSuggestions {
    fun weaponIds(builder: SuggestionsBuilder): SuggestionsBuilder {
        ItemRegistry.all()
            .filter { it.type == ItemType.WEAPON }
            .forEach { builder.suggest(it.id) }
        return builder
    }

    fun onlinePlayers(builder: SuggestionsBuilder): SuggestionsBuilder {
        Bukkit.getOnlinePlayers().forEach {
            builder.suggest(it.name)
        }
        return builder
    }
}
