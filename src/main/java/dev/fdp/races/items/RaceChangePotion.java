package dev.fdp.races.items;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.gui.RaceChangeGUI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;

public class RaceChangePotion implements Listener {
    public static final NamespacedKey RACE_CHANGE_POTION_KEY = new NamespacedKey(FDP_Races.getInstance(), "potion_change");

    public static ItemStack createCustomPotion() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();

        meta.setDisplayName("§dСменить рассу");
        meta.setColor(org.bukkit.Color.FUCHSIA);
        meta.getPersistentDataContainer().set(RACE_CHANGE_POTION_KEY, PersistentDataType.INTEGER, 1);

        potion.setItemMeta(meta);
        return potion;
    }

    @EventHandler
    public void onPotionDrink(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (item.getType() != Material.POTION) return;

        PotionMeta meta = (PotionMeta) item.getItemMeta();
        if (meta == null) return;

        if (meta.getPersistentDataContainer().has(RACE_CHANGE_POTION_KEY, PersistentDataType.INTEGER)) {
            Player player = event.getPlayer();
            RaceChangeGUI.open(player);
        }
    }
}
