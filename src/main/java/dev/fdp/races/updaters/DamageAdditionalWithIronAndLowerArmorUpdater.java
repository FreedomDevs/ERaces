package dev.fdp.races.updaters;

import dev.fdp.races.datatypes.Race;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class DamageAdditionalWithIronAndLowerArmorUpdater implements IUpdater, IUnloadable, Listener {

    private static final Map<String, Double> playerDamageAdditionalWithIronAndLowerArmor = new HashMap<>();

    @Override
    public void update(Race race, Player player) {
        double antiKnockbackLevelWithIronArmorAndMore = race.getWeaponProficiency()
                .getDamageAdditionalWithIronAndLowerArmor();

        if (antiKnockbackLevelWithIronArmorAndMore == 0) {
            playerDamageAdditionalWithIronAndLowerArmor.remove(player.getName());
        } else {
            playerDamageAdditionalWithIronAndLowerArmor.put(player.getName(), antiKnockbackLevelWithIronArmorAndMore);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            String playerName = event.getDamager().getName();
            if (playerDamageAdditionalWithIronAndLowerArmor.containsKey(playerName) && hasFullRequiredArmor(player)) {
                double originalDamage = event.getDamage();
                double bonusDamage = playerDamageAdditionalWithIronAndLowerArmor.get(playerName);
                double incrementDamage = originalDamage + bonusDamage;
                event.setDamage(incrementDamage);
            }
        }
    }

    @Override
    public void unload(Player player) {
        playerDamageAdditionalWithIronAndLowerArmor.remove(player.getName());
    }

    private boolean hasFullRequiredArmor(Player player) {
        return isIronOrBetter(player.getInventory().getHelmet()) &&
                isIronOrBetter(player.getInventory().getChestplate()) &&
                isIronOrBetter(player.getInventory().getLeggings()) &&
                isIronOrBetter(player.getInventory().getBoots());
    }

    private boolean isIronOrBetter(ItemStack item) {
        if (item == null)
            return false;
        return switch (item.getType()) {
            case LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS, CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS ->
                    true;
            default -> false;
        };
    }
}
