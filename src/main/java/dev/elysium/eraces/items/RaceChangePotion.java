package dev.elysium.eraces.items;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.gui.RaceChangeGui;
import dev.elysium.eraces.utils.ChatUtil;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
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
    public static final NamespacedKey RACE_CHANGE_POTION_KEY = new NamespacedKey(ERaces.getInstance(), "potion_change");


    public static ItemStack createCustomPotion() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();

        meta.displayName(ChatUtil.INSTANCE.text("Сменить расу", NamedTextColor.LIGHT_PURPLE));
        meta.setColor(Color.FUCHSIA);
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
            RaceChangeGui.open(player);
        }
    }
}
