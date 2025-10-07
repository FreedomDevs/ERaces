package dev.elysium.eraces.config;

import dev.elysium.eraces.utils.SqliteDatabase;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class SpecializationsManager {
    @Getter
    private final SpecializationsConfigManager specializationsConfigManager;
    private final SqliteDatabase database;

    public SpecializationsManager(JavaPlugin plugin, SqliteDatabase database) {
        specializationsConfigManager = new SpecializationsConfigManager(plugin);
        this.database = database;
        reloadConfig();
    }

    public void reloadConfig() {
        specializationsConfigManager.reloadConfig();
    }
}
