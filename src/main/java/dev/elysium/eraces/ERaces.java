package dev.elysium.eraces;

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
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
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
    private AbilsManager abilsManager;

    @SuppressWarnings("FieldMayHaveGetter")
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

    @Override
    public void onLoad() {
        instance = this;
        database = new SqliteDatabase();
        initManagers();
        loadConfigs();
    }

    @Override
    public void onEnable() {
        database.connect(this.getDataPath().toAbsolutePath()+"database_sqlite.db");
        try (Statement stmt = database.getConnection().createStatement()) {
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS races (
                        uuid TEXT PRIMARY KEY,
                        race_id TEXT
                    );
                """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        RacesReloader.startListeners(this);
        registerCommands();

        AbilsManager.init(this);

        if (globalConfigManager.getData().isDebug()) {
            getLogger().setLevel(Level.FINE);
        }

        getLogger().info(msg.getPluginEnabled());
    }

    @Override
    public void onDisable() {
        getLogger().info(msg.getPluginDisabled());
        database.close();
    }

    private void initManagers() {
        racesConfigManager = new RacesConfigManager(this);
        playerDataManager = new PlayerDataManager(this);
        playerDataManager = new PlayerDataManager(database);
        abilsManager = new AbilsManager(this);
        try {
            globalConfigManager = new GlobalConfigManager(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadConfigs() {
        String lang = globalConfigManager.getData().getLang();
        messageManager = new MessageManager(this, lang);
        msg = messageManager.getData();
    }

    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(new MyraceCommand().getCmd());
            commands.registrar().register(new RacesCommand().getCmd());
            commands.registrar().register(new AbilsCommand().getCmd());
        });
    }

    private void logInfo(String message) {
        getLogger().info(message);
    }
}
