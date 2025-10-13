package dev.elysium.eraces.updaters.speed;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.modifiers.ModifierAdapter;
import dev.elysium.eraces.modifiers.Modifiers;
import dev.elysium.eraces.utils.ArmorChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SlownessWithIronAndMoreArmorListener implements Listener {
    final ModifierAdapter<Integer> mod = Modifiers.SPEED.register();

    @EventHandler
    public void onArmorChange(PlayerArmorChangeEvent event) {
        int new_speed = 0;
        Player player = event.getPlayer();

        if (!ArmorChecker.allArmorLess(player, ArmorChecker.ArmorType.IRON)) {
            new_speed = -ERaces.getInstance()
                    .getContext()
                    .playerDataManager
                    .getPlayerRace(player)
                    .getSlownessWithIronAndMoreArmor();
        }

        mod.set(player, new_speed);
    }
}
