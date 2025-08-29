package dev.fdp.races.updaters;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import dev.fdp.races.FDP_Races;
import dev.fdp.races.utils.ArmorChecker;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AntiKnockbackLevelWithIronArmorAndMoreUpdater implements Listener {
    @EventHandler
    public void onArmorChange(PlayerArmorChangeEvent event) {
        Player player = event.getPlayer();
        int param = FDP_Races.getPlayerMng().getPlayerRace(player).getAntiKnocbackLevelWithIronArmorAndMore();
        if (param == 0) return;

        AttributeInstance attr = player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        if (attr != null) {
            if (ArmorChecker.allArmorGEQ(player, ArmorChecker.ArmorType.IRON)) {
                attr.setBaseValue(param);
            } else {
                attr.setBaseValue(0.0);
            }
        }
    }
}
