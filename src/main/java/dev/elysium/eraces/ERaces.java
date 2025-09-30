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
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class ERaces extends JavaPlugin {
    private static ERaces instance;

    private MessageManager messageManager;
    private RacesConfigManager racesConfigManager;
    private PlayerDataManager playerDataManager;
    private GlobalConfigManager globalConfigManager;
    private MessageConfigData msg;
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
        return getInstance().abilsManager;
    }

    @Override
    public void onLoad() {
        instance = this;
        initManagers();
        loadConfigs();
    }

    @Override
    public void onEnable() {
        RacesReloader.startListeners(this);
        registerCommands();

        abilsManager.init();

        if (globalConfigManager.getData().isDebug()) {
            getLogger().setLevel(Level.FINE);
        }

        logInfo(msg.getPluginEnabled());
    }

    @Override
    public void onDisable() {
        logInfo(msg.getPluginDisabled());
    }

    private void initManagers() {
        racesConfigManager = new RacesConfigManager(this);
        playerDataManager = new PlayerDataManager(this);
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
