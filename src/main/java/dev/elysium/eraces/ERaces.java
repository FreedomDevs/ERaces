package dev.elysium.eraces;

import dev.elysium.eraces.events.GuiListener;
import dev.elysium.eraces.events.RaceSelectListener;
import dev.elysium.eraces.abilities.AbilsManager;
import dev.elysium.eraces.commands.AbilsCommand;
import dev.elysium.eraces.commands.MyraceCommand;
import dev.elysium.eraces.commands.RacesCommand;
import dev.elysium.eraces.config.GlobalConfigManager;
import dev.elysium.eraces.config.MessageManager;
import dev.elysium.eraces.config.PlayerDataManager;
import dev.elysium.eraces.config.RacesConfigManager;
import dev.elysium.eraces.datatypes.configs.MessageConfigData;
import dev.elysium.eraces.utils.SqliteDatabase;
import dev.elysium.eraces.utils.targetUtils.PluginAccessor;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class ERaces extends JavaPlugin {
    private static ERaces instance;

    private MessageManager messageManager;
    private RacesConfigManager racesConfigManager;
    private PlayerDataManager playerDataManager;
    private GlobalConfigManager globalConfigManager;
    private MessageConfigData msg;
    private SqliteDatabase database;

    /* ------------------- Static Accessors ------------------- */

    public static ERaces getInstance() {
        if (instance == null) throw new IllegalStateException("ERaces не инициализирован!");
        return instance;
    }

    public static MessageConfigData getMsgMng() {
        return getInstance().messageManager.getData();
    }

    public static RacesConfigManager getRacesMng() {
        return getInstance().racesConfigManager;
    }

    public static PlayerDataManager getPlayerMng() {
        return getInstance().playerDataManager;
    }

    public static AbilsManager getABM() {
        return AbilsManager.getInstance();
    }

    /* ------------------- Lifecycle Methods ------------------- */

    @Override
    public void onLoad() {
        instance = this;
        database = new SqliteDatabase();
        initManagers();
        loadConfigs();
    }

    @Override
    public void onEnable() {
        connectDatabase();
        createTables();
        RacesReloader.startListeners(this);
        registerCommands();
        AbilsManager.init(this);
        configureLogging();
        PluginAccessor.INSTANCE.init(this);
        registerEventListeners();
        logInfo(msg.getPluginEnabled());
    }

    @Override
    public void onDisable() {
        logInfo(msg.getPluginDisabled());
        database.close();
    }

    /* ------------------- Initialization Helpers ------------------- */

    private void initManagers() {
        racesConfigManager = new RacesConfigManager(this);
        playerDataManager = new PlayerDataManager(database);
        try {
            globalConfigManager = new GlobalConfigManager(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to initialize GlobalConfigManager", e);
        }
    }

    private void loadConfigs() {
        String lang = globalConfigManager.getData().getLang();
        messageManager = new MessageManager(this, lang);
        msg = messageManager.getData();
    }

    /* ------------------- Database Helpers ------------------- */

    private void connectDatabase() {
        database.connect(this.getDataPath().toAbsolutePath() + "database_sqlite.db");
    }

    private void createTables() {
        try (Statement stmt = database.getConnection().createStatement()) {
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS races (
                        uuid TEXT PRIMARY KEY,
                        race_id TEXT
                    );
                    """);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create database tables", e);
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
        if (globalConfigManager.getData().isDebug()) {
            getLogger().setLevel(Level.FINE);
        }
    }

    private void logInfo(String message) {
        getLogger().info(message);
    }
}