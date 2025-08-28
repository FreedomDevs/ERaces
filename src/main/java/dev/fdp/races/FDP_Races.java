package dev.fdp.races;

import dev.fdp.races.commands.MyraceCommand;
import dev.fdp.races.commands.RacesCommand;
import dev.fdp.races.config.MessageManager;
import dev.fdp.races.config.PlayerDataManager;
import dev.fdp.races.config.RacesConfigManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class FDP_Races extends JavaPlugin {
    private MessageManager messageManager;
    private RacesConfigManager racesConfigManager;
    private PlayerDataManager playerDataManager;

    @Getter
    private static FDP_Races instance;

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
