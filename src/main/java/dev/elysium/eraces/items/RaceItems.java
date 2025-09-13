package dev.elysium.eraces.items;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.datatypes.RaceGuiConfig;
import dev.elysium.eraces.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class RaceItems {
    public static final NamespacedKey RACE_GUI_ITEM_KEY = new NamespacedKey(ERaces.getInstance(), "race_gui_item");

    public static ItemStack getItem(Race race, boolean isCurrent) {
        RaceGuiConfig cfg = race.getRaceGuiConfig();
        Material m = Material.getMaterial(cfg.getIcon());
        if (m == null) m = Material.DIRT;
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(ChatUtil.legacyFormat(cfg.getName()));
        meta.lore(Arrays.stream(cfg.getLore().split("\n")).map(ChatUtil::legacyFormat).toList());
        meta.getPersistentDataContainer().set(RACE_GUI_ITEM_KEY, PersistentDataType.STRING, race.getId());
        item.setItemMeta(meta);

        if (isCurrent) setHighlighted(item);
        return item;
    }

    private static void setHighlighted(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.BLAST_PROTECTION, 1, true);
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
    }
}