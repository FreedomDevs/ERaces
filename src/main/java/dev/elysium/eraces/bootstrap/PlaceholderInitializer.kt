package dev.elysium.eraces.bootstrap

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.ERaces.Companion.logger
import dev.elysium.eraces.exceptions.internal.InitFailedException
import dev.elysium.eraces.placeholders.ManaExpansion
import dev.elysium.eraces.placeholders.RaceExpansion
import dev.elysium.eraces.placeholders.SpecializationExpansion
import org.bukkit.Bukkit

class PlaceholderInitializer : IInitializer {
    override fun setup(plugin: ERaces) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            logger().warning("PlaceholderAPI не найден — плейсхолдеры отключены.")
            return
        }

        try {
            val expansions = listOf(
                ManaExpansion(plugin),
                SpecializationExpansion(plugin),
                RaceExpansion(plugin)
            )

            for (expansion in expansions) {
                expansion.register()
                logger().info("Зарегистрирован плейсхолдер: " + expansion.identifier)
            }

            logger().info("PlaceholderAPI expansion 'eraces' успешно зарегистрирован.")
        } catch (e: Exception) {
            throw InitFailedException("Ошибка при регистрации плейсхолдеров", e)
        }
    }
}
