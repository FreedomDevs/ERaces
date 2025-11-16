package dev.elysium.eraces;

import dev.elysium.eraces.bootstrap.*;
import dev.elysium.eraces.exceptions.InitFailedException;
import dev.elysium.eraces.abilities.AbilsManager;
import dev.elysium.eraces.gui.raceSelect.RaceSelectMenuPages;
import dev.elysium.eraces.utils.SqliteDatabase;
import dev.elysium.eraces.utils.targetUtils.PluginAccessor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

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

        List<IInitializer> initializers = List.of(
                new DatabaseInitializer(),
                new ManagerInitializer(),
                new ConfigInitializer(),
                new PlaceholderInitializer(),
                new CommandInitializer(),
                new ListenerInitializer(),
                new LoggerConfigurator()
        );

        for (IInitializer initializer : initializers) {
            try {
                initializer.setup(this);
            } catch (InitFailedException e) {
                getLogger().severe("Ошибка инициализации " + initializer.getClass().getSimpleName() + ": " + e.getMessage());
                getServer().getPluginManager().disablePlugin(this);
                return;
            } catch (Exception e) {
                getLogger().severe("Неожиданная ошибка в " + initializer.getClass().getSimpleName());
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }

        AbilsManager.init(this);
        PluginAccessor.INSTANCE.init(this);

        logInfo(context.getMessageManager().getData().getPluginEnabled());
    }

    @Override
    public void onDisable() {
        logInfo(context.getMessageManager().getData().getPluginDisabled());
        context.getDatabase().close();
    }

    public void logInfo(String message) {
        getLogger().info(message);
    }
}
