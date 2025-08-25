package dev.fdp.races.updaters;

import dev.fdp.races.datatypes.Race;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

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


    private void updateKnockbackAttribute(Player player) {
        int level = playerAntiKnockbackLevelWithIronArmorAndMore.get(player.getName());
        AttributeInstance attr = player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        if (attr != null) {
            if (hasFullRequiredArmor(player)) {
                attr.setBaseValue(level);
            } else {
                attr.setBaseValue(0.0);
            }
        }
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
            case IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS, DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS, NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS ->
                    true;
            default -> false;
        };
    }
}
