package dev.elysium.eraces;

import dev.elysium.eraces.listeners.GuiListener;
import dev.elysium.eraces.listeners.PluginMessageListener;
import dev.elysium.eraces.listeners.RaceSelectListener;
import dev.elysium.eraces.abilities.AbilsManager;
import dev.elysium.eraces.commands.AbilsCommand;
import dev.elysium.eraces.commands.MyraceCommand;
import dev.elysium.eraces.commands.RacesCommand;
import dev.elysium.eraces.config.*;
import dev.elysium.eraces.config.GlobalConfigManager;
import dev.elysium.eraces.config.MessageManager;
import dev.elysium.eraces.config.PlayerDataManager;
import dev.elysium.eraces.config.RacesConfigManager;
import dev.elysium.eraces.gui.raceSelect.RaceSelectMenuPages;
import dev.elysium.eraces.placeholders.ManaExpansion;
import dev.elysium.eraces.placeholders.SpecializationExpansion;
import dev.elysium.eraces.utils.SqliteDatabase;
import dev.elysium.eraces.utils.targetUtils.PluginAccessor;
import dev.elysium.eraces.xpManager.XpManager;
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
        RaceSelectMenuPages.INSTANCE.registerDefaults();
        RacesReloader.startListeners(this);

        connectDatabase();
        createTables();

        initManagers();
        loadConfigs();

        AbilsManager.init(this);
        PluginAccessor.INSTANCE.init(this);

        registerCommands();
        registerEventListeners();
        registerPlaceholders();

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

        XpManager xpManager = new XpManager();
        Bukkit.getPluginManager().registerEvents(xpManager, this);
        context.setXpManager(xpManager);

        DamageTracker damageTracker = new DamageTracker();
        Bukkit.getPluginManager().registerEvents(damageTracker, this);
        context.setXpDamageTracker(damageTracker);

        context.manaManager = new ManaManager(this);
    }

    private void loadConfigs() {
        String lang = context.getGlobalConfigManager().getData().getLang();
        MessageManager messageManager = new MessageManager(this, lang);
        context.setMessageManager(messageManager);
    }

    /* ------------------- Placeholders init ------------------- */

    private void registerPlaceholders() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().warning("PlaceholderAPI не найден — плейсхолдеры отключены.");
            return;
        }

        try {
            new ManaExpansion(this).register();
            new SpecializationExpansion(this).register();
            getLogger().info("PlaceholderAPI expansion 'eraces' успешно зарегистрирован.");
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Ошибка при регистрации плейсхолдеров PlaceholderAPI", e);
        }
    }

    /* ------------------- Database Helpers ------------------- */

    private void connectDatabase() {
        getDataFolder().mkdirs();
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
                              specialization TEXT NOT NULL,    -- Выбранная специализация
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
        getServer().getMessenger().registerIncomingPluginChannel(this, "elysium:eraces_cast", new PluginMessageListener());

        GuiListener.INSTANCE.init(this);
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
