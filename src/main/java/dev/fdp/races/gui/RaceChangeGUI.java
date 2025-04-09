package dev.fdp.races.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RaceChangeGUI implements Listener {
    public static void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§5Меню смены рассы");

        ItemStack button1 = new ItemStack(Material.DIAMOND);
        ItemMeta meta1 = button1.getItemMeta();
        meta1.setDisplayName("§aНаграда 1");
        button1.setItemMeta(meta1);
        gui.setItem(3, button1);

        ItemStack button2 = new ItemStack(Material.EMERALD);
        ItemMeta meta2 = button2.getItemMeta();
        meta2.setDisplayName("§bНаграда 2");
        button2.setItemMeta(meta2);
        gui.setItem(5, button2);

        player.openInventory(gui);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("§5Меню Зелья")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.AIR) return;

            String name = clicked.getItemMeta().getDisplayName();
            switch (name) {
                case "§aНаграда 1":
                    player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
                    player.sendMessage("§aТы получил Награду 1!");
                    break;
                case "§bНаграда 2":
                    player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
                    player.sendMessage("§bТы получил Награду 2!");
                    break;
            }

            player.closeInventory();
        }
    }
}
