package dev.fdp.races.updaters;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.datatypes.Race;
import dev.fdp.races.utils.ChatUtil;
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
                        .sendActionBar(ChatUtil.format(FDP_Races.getMsgMng().getString("shield_block"), Map.of()));

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
        String playername = player.getName();

        if (race.isShieldUsage()) {
            blockedShield.remove(playername);
        } else {
            blockedShield.add(playername);
        }
    }

    @Override
    public void unload(Player player) {
        blockedShield.remove(player.getName());
    }
}
