package dev.elysium.eraces.updaters;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.updaters.base.IUnloadable;
import dev.elysium.eraces.updaters.base.IUpdater;
import dev.elysium.eraces.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ShieldUsageUpdater implements Listener, IUpdater, IUnloadable {
    private static final Set<String> blockedShield = new HashSet<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (blockedShield.contains(event.getPlayer().getName())) {
            if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
                    && event.getItem() != null
                    && event.getItem().getType() == Material.SHIELD) {

                event.getPlayer()
                        .sendActionBar(ChatUtil.INSTANCE.legacyFormat(ERaces.getInstance().getContext().messageManager.getData().getShieldBlock(), Map.of()));

                event.getPlayer().playSound(
                        event.getPlayer().getLocation(),
                        Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR,
                        1.0f,
                        1.0f);

                event.setCancelled(true);
            }
        }
    }

    @Override
    public void update(Race race, Player player) {
        String playerName = player.getName();

        if (race.getShieldUsage()) {
            blockedShield.remove(playerName);
        } else {
            blockedShield.add(playerName);
        }
    }

    @Override
    public void unload(Player player) {
        blockedShield.remove(player.getName());
    }
}
