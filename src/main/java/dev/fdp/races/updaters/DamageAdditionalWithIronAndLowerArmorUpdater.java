package dev.fdp.races.updaters;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import dev.fdp.races.Race;

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
        if (event.getDamager() instanceof Player) {
            String playerName = event.getDamager().getName();
            Player player = ((Player) event.getDamager());
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
        Material type = item.getType();
        switch (type) {
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
            case CHAINMAIL_HELMET:
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_LEGGINGS:
            case CHAINMAIL_BOOTS:
                return true;
            default:
                return false;
        }
    }
}
