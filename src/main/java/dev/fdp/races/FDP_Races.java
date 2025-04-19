package dev.fdp.races;

import java.util.Map;

import dev.fdp.races.commands.RacesCommand;
import dev.fdp.races.events.*;
import dev.fdp.races.gui.RaceChangeGUI;
import dev.fdp.races.items.RaceChangePotion;
import dev.fdp.races.updaters.*;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class FDP_Races extends JavaPlugin {
    public Map<String, Race> races = null;
    public RaceManager raceManager;
    public MessageManager messageManager;
    private static FDP_Races instance;
    private BiomeSpeedUpdater biomeSpeedUpdater;
    private PeacefulMobsAfraidUpdater peacefulMobsAfraidUpdater;

    @Override
    public void onLoad() {
        instance = this;
    }

    public static FDP_Races getInstance() {
        return instance;
    }

    public void reloadConfig() {
        RacesConfigLoader.checkConfigExists(this);
        this.races = RacesConfigLoader.loadConfig(this);
        getLogger().info("Загружено: " + this.races.size() + " расс");
    }

    @Override
    public void onEnable() {
        messageManager = new MessageManager(this);

        reloadConfig();

        raceManager = new RaceManager(getDataFolder(), this);

        Listener[] listeners = {
                new PlayerJoinListener(),
                new PlayerRespawnListener(),
                new RacesReloader(),
                new ShieldUsageUpdater(),
                new ForbiddenFoodsUpdater(),
                new HandDamageUpdater(),
                new AxeDamageUpdater(),
                new SwordDamageUpdater(),
                new BowDamageUpdater(),
                new RaceChangeGUI(),
                new RaceChangePotion(),
                new BowSpeedUpdater(),
                new MaceDamageUpdater(),
                new DualWeaponDamageUpdater()

        };

        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }

        startTasks();
        registerCommand();

        RacesReloader.reloadRaceForAllPlayers();

        getLogger().info(messageManager.getString("plugin.enabled"));
    }

    @Override
    public void onDisable() {
        getLogger().info(messageManager.getString("plugin.disabled", "Выключение😞"));
    }

    private void startTasks() {
        biomeSpeedUpdater = new BiomeSpeedUpdater();
        peacefulMobsAfraidUpdater = new PeacefulMobsAfraidUpdater();

        biomeSpeedUpdater.startTask(this);
        peacefulMobsAfraidUpdater.startTask(this);
    }

    private void registerCommand() {
        RacesCommand racesCommand = new RacesCommand();
        getCommand("races").setExecutor(racesCommand);
        getCommand("races").setTabCompleter(racesCommand);
    }
}
