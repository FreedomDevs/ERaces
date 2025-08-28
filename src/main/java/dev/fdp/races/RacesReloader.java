package dev.fdp.races;

import dev.fdp.races.datatypes.Race;
import dev.fdp.races.events.*;
import dev.fdp.races.items.RaceChangePotion;
import dev.fdp.races.updaters.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class RacesReloader implements Listener {

    private static final List<Object> updaters = List.of(
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
            new DamageAdditionalWithLowerThanIronArmorUpdater(),
            new BlockReachUpdater(),
            new DamageWithWolfsNearUpdater(),

            new PlayerJoinListener(),
            new PlayerQuitListener(),
            new PlayerRespawnListener(),
            new RaceChangeGuiListener(),
            new RaceChangePotion(),
            new SaturationUpdater(),
            new SlownessWithIronAndMoreArmorListener()
    );

    public static void reloadRaceForPlayer(Player player) {
        Race race = FDP_Races.getPlayerMng().getPlayerRace(player);
        for (Object obj : updaters) {
            if (obj instanceof IUpdater upd)
                upd.update(race, player);
        }
    }

    public static void reloadRaceForAllPlayers() {
        for (Player i : Bukkit.getOnlinePlayers()) {
            reloadRaceForPlayer(i);
        }
    }

    public static void startListeners(JavaPlugin plugin) { // Ищет все листенеры в updaters и включает их
        for (Object obj : updaters)
            if (obj instanceof Listener lis)
                Bukkit.getPluginManager().registerEvents(lis, plugin);
    }

    public static void unloadPlayerData(Player player) { // Типо удаляет мусор из ОЗУ
        for (Object obj : updaters)
            if (obj instanceof IUnloadable unl)
                unl.unload(player);
    }
}
