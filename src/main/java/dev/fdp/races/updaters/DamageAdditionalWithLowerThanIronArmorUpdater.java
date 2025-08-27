package dev.fdp.races.updaters;

import dev.fdp.races.datatypes.Race;
import dev.fdp.races.utils.ArmorChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class DamageAdditionalWithLowerThanIronArmorUpdater implements IUpdater, IUnloadable, Listener {

    private static final Map<String, Double> playerDamageAdditionalWithLowerThanIronArmor = new HashMap<>();

    @Override
    public void update(Race race, Player player) {
        double damage = race.getWeaponProficiency()
                .getDamageAdditionalWithIronAndLowerArmor();

        if (damage == 0) {
            playerDamageAdditionalWithLowerThanIronArmor.remove(player.getName());
        } else {
            playerDamageAdditionalWithLowerThanIronArmor.put(player.getName(), damage);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            String playerName = event.getDamager().getName();
            if (playerDamageAdditionalWithLowerThanIronArmor.containsKey(playerName) && ArmorChecker.allArmorLess(player, ArmorChecker.ArmorType.IRON)) {
                double originalDamage = event.getDamage();
                double bonusDamage = playerDamageAdditionalWithLowerThanIronArmor.get(playerName);
                double incrementDamage = originalDamage + bonusDamage;
                event.setDamage(incrementDamage);
            }
        }
    }

    @Override
    public void unload(Player player) {
        playerDamageAdditionalWithLowerThanIronArmor.remove(player.getName());
    }
}
