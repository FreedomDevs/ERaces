package dev.elysium.eraces;

import dev.elysium.eraces.commands.MyraceCommand;
import dev.elysium.eraces.commands.RacesCommand;
import dev.elysium.eraces.config.GlobalConfigManager;
import dev.elysium.eraces.config.MessageManager;
import dev.elysium.eraces.config.PlayerDataManager;
import dev.elysium.eraces.config.RacesConfigManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class ERaces extends JavaPlugin {
    private static ERaces instance;

    private MessageManager messageManager;
    private RacesConfigManager racesConfigManager;
    private PlayerDataManager playerDataManager;
    private GlobalConfigManager globalConfigManager;
    @SuppressWarnings("FieldMayHaveGetter")
    public static ERaces getInstance() {
        return instance;
    }

    public static RacesConfigManager getRacesMng() {
        return instance.racesConfigManager;
    }

    public static PlayerDataManager getPlayerMng() {
        return instance.playerDataManager;
    }

    public static GlobalConfigManager getGlobalCfg() {
        return instance.globalConfigManager;
    }

    @Override
    public void onLoad() {
        instance = this;

        racesConfigManager = new RacesConfigManager(this);
        playerDataManager = new PlayerDataManager(this);

        try {
            globalConfigManager = new GlobalConfigManager(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        messageManager = new MessageManager();
    }

    @Override
    public void onEnable() {
        RacesReloader.startListeners(this);
        registerCommands();

        if (globalConfigManager.getData().isDebug()) {
            getLogger().setLevel(Level.FINE);
        }

        getLogger().info(msg.getPluginEnabled());
    }

    @Override
    public void onDisable() {
        getLogger().info(msg.getPluginDisabled());
    }

    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(new MyraceCommand().getCmd());
            commands.registrar().register(new RacesCommand().getCmd());
        });
    }
}
