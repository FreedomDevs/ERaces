package dev.elysium.eraces.updaters;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.utils.ArmorChecker;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AntiKnockbackLevelWithIronArmorAndMoreUpdater implements Listener {

    @EventHandler
    public void onArmorChange(PlayerArmorChangeEvent event) {
        Player player = event.getPlayer();
        int param = ERaces.getInstance()
                .getContext()
                .playerDataManager
                .getPlayerRace(player)
                .getAntiKnocbackLevelWithIronArmorAndMore();

        if (param == 0) return;

        AttributeInstance attr = player.getAttribute(Attribute.KNOCKBACK_RESISTANCE);
        if (attr != null) {
            if (ArmorChecker.allArmorGEQ(player, ArmorChecker.ArmorType.IRON)) {
                attr.setBaseValue(param);
            } else {
                attr.setBaseValue(0.0);
            }
        }
    }
}
