package dev.fdp.races;

import dev.fdp.races.config.PlayerDataManager;
import dev.fdp.races.updaters.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class RacesReloader implements Listener {

    private static final List<IUpdater> updaters = List.of(
            new HealthUpdater(),
            new MineSpeedUpdater(),
            new AdditionalArmorUpdater(),
            new HealthRegenUpdater(),
            new ShieldUsageUpdater(),
            new ForbiddenFoodsUpdater(),
            new HandDamageUpdater(),
            new MovementSpeedUpdater(),
            new AntiKnockbackLevelUpdater(),
            new BiomeSpeedUpdater(),
            new AxeDamageUpdater(),
            new SwordDamageUpdater(),
            new BowDamageUpdater(),
            new PeacefulMobsAfraidUpdater(),
            new BowSpeedUpdater(),
            new MaceDamageUpdater(),
            new DualWeaponDamageUpdater(),
            new DamageResistanceLevelUpdater(),
            new DamageUpdater(),
            new AntiKnockbackLevelWithIronArmorAndMoreUpdater(),
            new DamageAdditionalWithIronAndLowerArmorUpdater(),
            new BlockReachUpdater(),
            new DamageWithWolfsNearUpdater());

    public static void reloadRaceForPlayer(Player player) {
        PlayerDataManager playerDataManager = FDP_Races.getInstance().getPlayerDataManager();

        for (IUpdater updater : updaters) {
            updater.update(playerDataManager.getPlayerRace(player.getName()), player);
        }
    }

    public static void reloadRaceForAllPlayers() {
        for (Player i : Bukkit.getOnlinePlayers()) {
            reloadRaceForPlayer(i);
        }
    }

    public static void startListeners(JavaPlugin plugin) { // Ищет все листенеры в updaters и включает их
        Bukkit.getPluginManager().registerEvents(new RacesReloader(), plugin);
        for (IUpdater i : updaters) {
            if (i instanceof Listener) {
                Bukkit.getPluginManager().registerEvents((Listener) i, plugin);
            }
        }
    }

    private static void unloadPlayerData(Player player) { // Типо удаляет мусор из ОЗУ
        for (IUpdater i : updaters) {
            if (i instanceof IUnloadable unloadable) {
                unloadable.unload(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        unloadPlayerData(event.getPlayer());
    }
}
