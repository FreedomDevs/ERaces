package dev.fdp.races.updaters;

import dev.fdp.races.datatypes.Race;
import dev.fdp.races.utils.ArmorChecker;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class AntiKnockbackLevelWithIronArmorAndMoreUpdater implements IUpdater, IUnloadable, Listener {

    private static final Map<String, Integer> playerAntiKnockbackLevelWithIronArmorAndMore = new HashMap<>();

    @Override
    public void update(Race race, Player player) {
        int antiKnockbackLevelWithIronArmorAndMore = race.getAntiKnocbackLevelWithIronArmorAndMore();

        if (antiKnockbackLevelWithIronArmorAndMore == 0) {
            playerAntiKnockbackLevelWithIronArmorAndMore.remove(player.getName());
        } else {
            playerAntiKnockbackLevelWithIronArmorAndMore.put(player.getName(), antiKnockbackLevelWithIronArmorAndMore);
        }

        if (playerAntiKnockbackLevelWithIronArmorAndMore.containsKey(player.getName())) {
            updateKnockbackAttribute(player);
        }
    }

    @Override
    public void unload(Player player) {
        playerAntiKnockbackLevelWithIronArmorAndMore.remove(player.getName());
    }

    // TODO: добавить в планировщик
    private void updateKnockbackAttribute(Player player) {
        int level = playerAntiKnockbackLevelWithIronArmorAndMore.get(player.getName());
        AttributeInstance attr = player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        if (attr != null) {
            if (ArmorChecker.allArmorGEQ(player, ArmorChecker.ArmorType.IRON)) {
                attr.setBaseValue(level);
            } else {
                attr.setBaseValue(0.0);
            }
        }
    }
}
