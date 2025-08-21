package dev.fdp.races;

import dev.fdp.races.commands.GetOwnRaceCommand;
import dev.fdp.races.commands.RacesCommand;
import dev.fdp.races.config.MessageManager;
import dev.fdp.races.config.PlayerDataManager;
import dev.fdp.races.config.RacesConfigManager;
import dev.fdp.races.events.PlayerJoinListener;
import dev.fdp.races.events.PlayerRespawnListener;
import dev.fdp.races.events.RaceChangeGuiListener;
import dev.fdp.races.items.RaceChangePotion;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class FDP_Races extends JavaPlugin {
    @Getter
    private PlayerDataManager playerDataManager;
    @Getter
    private MessageManager messageManager;
    @Getter
    private RacesConfigManager racesConfigManager;

    @Getter
    private static FDP_Races instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        messageManager = new MessageManager(this);
        racesConfigManager = new RacesConfigManager(this);
        playerDataManager = new PlayerDataManager(this);

        Listener[] listeners = {
                new PlayerJoinListener(),
                new PlayerRespawnListener(),
                new RaceChangeGuiListener(),
                new RaceChangePotion()
        };

        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }

        RacesReloader.startListeners(this);
        registerCommands();

        getLogger().info(messageManager.getString("plugin.enabled"));
    }

    @Override
    public void onDisable() {
        getLogger().info(messageManager.getString("plugin.disabled", "Выключение😞"));
    }

    private void registerCommands() {
        RacesCommand racesCommand = new RacesCommand();
        getCommand("races").setExecutor(racesCommand);
        getCommand("races").setTabCompleter(racesCommand);
        getCommand("myrace").setExecutor(new GetOwnRaceCommand());
    }
}
