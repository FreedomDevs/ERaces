package dev.elysium.eraces;

import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.events.*;
import dev.elysium.eraces.updaters.*;
import dev.elysium.eraces.updaters.damage.*;
import dev.elysium.eraces.items.RaceChangePotion;
import dev.elysium.eraces.updaters.base.IUnloadable;
import dev.elysium.eraces.updaters.base.IUpdater;
import dev.elysium.eraces.updaters.speed.SlownessWithIronAndMoreArmorListener;
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
            new AntiKnockbackLevelUpdater(),
            new AxeDamageUpdater(),
            new SwordDamageUpdater(),
            new BowDamageUpdater(),
            new PeacefulMobsAfraidUpdater(),
            new BowSpeedUpdater(),
            new MaceDamageUpdater(),
            new DualWeaponDamageUpdater(),
            new DamageUpdater(),
            new AntiKnockbackLevelWithIronArmorAndMoreUpdater(),
            new DamageAdditionalWithLowerThanIronArmorUpdater(),
            new BlockReachUpdater(),
            new DamageWithWolfsNearUpdater(),
            new SaturationUpdater(),
            new SlownessWithIronAndMoreArmorListener(),
            new EffectsUpdater(),
            new NeutralMobsUpdater(),

            new PlayerJoinListener(),
            new PlayerQuitListener(),
            new PlayerRespawnListener(),
            new RaceChangeGuiListener(),
            new RaceChangePotion(),
            new PlayerShootBowEventListener()
    );

    public static void reloadRaceForPlayer(Player player) {
        Race race = ERaces.getPlayerMng().getPlayerRace(player);
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
