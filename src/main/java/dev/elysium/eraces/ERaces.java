package dev.elysium.eraces;

import dev.elysium.eraces.events.GuiListener;
import dev.elysium.eraces.events.RaceSelectListener;
import dev.elysium.eraces.abilities.AbilsManager;
import dev.elysium.eraces.commands.AbilsCommand;
import dev.elysium.eraces.commands.MyraceCommand;
import dev.elysium.eraces.commands.RacesCommand;
import dev.elysium.eraces.config.*;
import dev.elysium.eraces.datatypes.configs.MessageConfigData;
import dev.elysium.eraces.config.GlobalConfigManager;
import dev.elysium.eraces.config.MessageManager;
import dev.elysium.eraces.config.PlayerDataManager;
import dev.elysium.eraces.config.RacesConfigManager;
import dev.elysium.eraces.utils.SqliteDatabase;
import dev.elysium.eraces.utils.targetUtils.PluginAccessor;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class ERaces extends JavaPlugin {
    private static ERaces instance;

    private PluginContext context;

    public static ERaces getInstance() {
        if (instance == null) throw new IllegalStateException("ERaces не инициализирован!");
        return instance;
    }

    public PluginContext getContext() {
        return context;
    }

    @Override
    public void onLoad() {
        instance = this;

        SqliteDatabase database = new SqliteDatabase();
        context = new PluginContext(database);
    }

    @Override
    public void onEnable() {
        connectDatabase();
        createTables();

        initManagers();
        loadConfigs();

        AbilsManager.init(this);
        PluginAccessor.INSTANCE.init(this);

        registerCommands();
        registerEventListeners();

        configureLogging();

        logInfo(context.getMessageManager().getData().getPluginEnabled());
    }

    @Override
    public void onDisable() {
        logInfo(context.getMessageManager().getData().getPluginDisabled());
        context.getDatabase().close();
    }

    /* ------------------- Initialization Helpers ------------------- */

    private void initManagers() {
        try {
            GlobalConfigManager globalConfig = new GlobalConfigManager(this);
            context.setGlobalConfigManager(globalConfig);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Не удалось инициализировать GlobalConfigManager", e);
        }

        context.setSpecializationsManager(new SpecializationsManager(this, context.getDatabase()));
        context.setRacesConfigManager(new RacesConfigManager(this));
        context.setPlayerDataManager(new PlayerDataManager(context.getRacesConfigManager().getRaces(), context.getDatabase()));
    }

    private void loadConfigs() {
        String lang = context.getGlobalConfigManager().getData().getLang();
        MessageManager messageManager = new MessageManager(this, lang);
        context.setMessageManager(messageManager);
    }

    /* ------------------- Database Helpers ------------------- */

    private void connectDatabase() {
        Path dbPath = getDataFolder().toPath().resolve("database_sqlite.db");
        context.getDatabase().connect(dbPath.toString());
    }

    private void createTables() {
        try (Statement stmt = context.getDatabase().getConnection().createStatement()) {
            stmt.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS races (
                            uuid TEXT PRIMARY KEY,
                            race_id TEXT
                        );
                    """);
            stmt.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS specialization_levels (
                              uuid TEXT PRIMARY KEY,  -- UUID игрока
                              specialization TEXT,    -- Выбранная специализация
                              level INTEGER NOT NULL, -- уровень
                              xp INTEGER NOT NULL,    -- опыт
                              int REAL NOT NULL,      -- сила интеллекта
                              str REAL NOT NULL,      -- сила
                              agi REAL NOT NULL,      -- ловкость
                              vit REAL NOT NULL       -- выносливость
                          );
                    """);
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось создать таблицы БД", e);
        }
    }

    /* ------------------- Command & Event Registration ------------------- */

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(new MyraceCommand().getCmd());
            commands.registrar().register(new RacesCommand().getCmd());
            commands.registrar().register(new AbilsCommand().getCmd());
        });
    }

    private void registerEventListeners() {
        Bukkit.getPluginManager().registerEvents(RaceSelectListener.INSTANCE, this);
        Bukkit.getPluginManager().registerEvents(GuiListener.INSTANCE, this);
    }

    /* ------------------- Utility Methods ------------------- */

    private void configureLogging() {
        if (context.globalConfigManager.getData().isDebug()) {
            getLogger().setLevel(Level.FINE);
        }
    }

    public void logInfo(String message) {
        getLogger().info(message);
    }
}
