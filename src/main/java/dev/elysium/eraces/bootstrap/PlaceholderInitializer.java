package dev.elysium.eraces.bootstrap;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.exceptions.InitFailedException;
import dev.elysium.eraces.placeholders.ManaExpansion;
import dev.elysium.eraces.placeholders.RaceExpansion;
import dev.elysium.eraces.placeholders.SpecializationExpansion;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;

import java.util.List;

public class PlaceholderInitializer implements IInitializer {
    @Override
    public void setup(ERaces plugin) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            plugin.getLogger().warning("PlaceholderAPI не найден — плейсхолдеры отключены.");
            return;
        }

        try {
            List<PlaceholderExpansion> expansions = List.of(
                    new ManaExpansion(plugin),
                    new SpecializationExpansion(plugin),
                    new RaceExpansion(plugin)
            );

            for (PlaceholderExpansion expansion : expansions) {
                expansion.register();
                plugin.getLogger().info("Зарегистрирован плейсхолдер: " + expansion.getIdentifier());
            }

            plugin.getLogger().info("PlaceholderAPI expansion 'eraces' успешно зарегистрирован.");
        } catch (Exception e) {
            throw new InitFailedException("Ошибка при регистрации плейсхолдеров", e);
        }
    }
}
