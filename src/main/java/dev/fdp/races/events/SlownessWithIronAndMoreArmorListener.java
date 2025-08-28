package dev.fdp.races.events;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import dev.fdp.races.FDP_Races;
import dev.fdp.races.modifiers.ModifierAdapter;
import dev.fdp.races.modifiers.Modifiers;
import dev.fdp.races.utils.ArmorChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SlownessWithIronAndMoreArmorListener implements Listener {
    final ModifierAdapter mod = Modifiers.SPEED.register();


    @EventHandler
    public void onArmorChange(PlayerArmorChangeEvent event) {
        int new_speed = 0;
        Player player = event.getPlayer();
        if (!ArmorChecker.allArmorLess(player, ArmorChecker.ArmorType.IRON)) {
            new_speed = -FDP_Races.getPlayerMng().getPlayerRace(player).getSlownessWithIronAndMoreArmor();
        }
        mod.set(player, new_speed);
    }
}
