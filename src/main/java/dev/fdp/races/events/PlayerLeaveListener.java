package dev.fdp.races.events;

import dev.fdp.races.updaters.ForbiddenFoodsUpdater;
import dev.fdp.races.updaters.HandDamageUpdater;
import dev.fdp.races.updaters.IUnloadable;
import dev.fdp.races.updaters.ShieldUsageUpdater;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        List<IUnloadable> unloadable = List.of(
                new ForbiddenFoodsUpdater(),
                new HandDamageUpdater(),
                new ShieldUsageUpdater()

        );

        for (IUnloadable i : unloadable) {
            i.unload(player);
        }
    }
}
