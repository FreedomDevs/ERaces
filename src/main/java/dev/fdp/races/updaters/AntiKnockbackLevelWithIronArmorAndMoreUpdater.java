package dev.fdp.races.updaters;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import dev.fdp.races.Race;

import java.util.HashMap;
import java.util.Map;

public class AntiKnockbackLevelWithIronArmorAndMoreUpdater implements IUpdater, IUnloadable, Listener {

  private static Map<String, Integer> playerAntiKnockbackLevelWithIronArmorAndMore = new HashMap<>();

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

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if (!(event.getWhoClicked() instanceof Player))
      return;

    Player player = (Player) event.getWhoClicked();

    if (playerAntiKnockbackLevelWithIronArmorAndMore.containsKey(player.getName())) {
      updateKnockbackAttribute(player);
    }

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
    Material type = item.getType();
    switch (type) {
      case IRON_HELMET:
      case IRON_CHESTPLATE:
      case IRON_LEGGINGS:
      case IRON_BOOTS:
      case DIAMOND_HELMET:
      case DIAMOND_CHESTPLATE:
      case DIAMOND_LEGGINGS:
      case DIAMOND_BOOTS:
      case NETHERITE_HELMET:
      case NETHERITE_CHESTPLATE:
      case NETHERITE_LEGGINGS:
      case NETHERITE_BOOTS:
        return true;
      default:
        return false;
    }
  }
}
