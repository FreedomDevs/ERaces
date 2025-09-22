package dev.elysium.eraces;

import dev.elysium.eraces.commands.MyraceCommand;
import dev.elysium.eraces.commands.RacesCommand;
import dev.elysium.eraces.config.MessageManager;
import dev.elysium.eraces.config.PlayerDataManager;
import dev.elysium.eraces.config.RacesConfigManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class ERaces extends JavaPlugin {
    private MessageManager messageManager;
    private RacesConfigManager racesConfigManager;
    private PlayerDataManager playerDataManager;

    private static ERaces instance;
    public static ERaces getInstance() {
        return instance;
    }

    public static MessageManager getMsgMng() {
        return instance.messageManager;
    }

    public static RacesConfigManager getRacesMng() {
        return instance.racesConfigManager;
    }

    public static PlayerDataManager getPlayerMng() {
        return instance.playerDataManager;
    }

    @Override
    public void onLoad() {
        instance = this;
        messageManager = new MessageManager(this);
        racesConfigManager = new RacesConfigManager(this);
        playerDataManager = new PlayerDataManager(this);
    }

    @Override
    public void onEnable() {
        RacesReloader.startListeners(this);
        registerCommands();

        getLogger().info(messageManager.getString("plugin.enabled"));
    }

    @Override
    public void onDisable() {
        getLogger().info(messageManager.getString("plugin.disabled", "Выключение😞"));
    }

    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(new MyraceCommand().getCmd());
            commands.registrar().register(new RacesCommand().getCmd());
        });
    }
}
