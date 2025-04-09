package dev.fdp.races.items;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class RaceItems {
    private static ItemStack setHighlightedRace(ItemStack item, boolean isSelected) {
        ItemMeta meta = item.getItemMeta();

        if (isSelected) {
            meta.addEnchant(Enchantment.BLAST_PROTECTION, 1, true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack elf(String currentRace) {
        ItemStack item = new ItemStack(Material.OAK_SAPLING);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§aЭльф");

        List<String> lore = Arrays.asList(
                "- Нажмите чтобы выбрать"
        );

        meta.setLore(lore);
        item.setItemMeta(meta);
        return setHighlightedRace(item, currentRace.equals("elf"));
    }

    public static ItemStack human(String currentRace) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§eЧеловек");

        List<String> lore = Arrays.asList(
                "- Нажмите чтобы выбрать"
        );

        meta.setLore(lore);
        item.setItemMeta(meta);
        return setHighlightedRace(item, currentRace.equals("human"));
    }

    public static ItemStack bearBeastman(String currentRace) {
        ItemStack item = new ItemStack(Material.BROWN_WOOL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Зверолюд-медведь");

        List<String> lore = Arrays.asList(
                "- Нажмите чтобы выбрать"
        );

        meta.setLore(lore);
        item.setItemMeta(meta);
        return setHighlightedRace(item, currentRace.equals("bear_beastman"));
    }

    public static ItemStack wolfBeastman(String currentRace) {
        ItemStack item = new ItemStack(Material.GRAY_WOOL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§7Зверолюд-волк");

        List<String> lore = Arrays.asList(
                "- Нажмите чтобы выбрать"
        );

        meta.setLore(lore);
        item.setItemMeta(meta);
        return setHighlightedRace(item, currentRace.equals("wolf_beastman"));
    }

    public static ItemStack tigerBeastman(String currentRace) {
        ItemStack item = new ItemStack(Material.ORANGE_WOOL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Зверолюд-тигр");

        List<String> lore = Arrays.asList(
                "- Нажмите чтобы выбрать"
        );

        meta.setLore(lore);
        item.setItemMeta(meta);
        return setHighlightedRace(item, currentRace.equals("tiger_beastman"));
    }

    public static ItemStack ork(String currentRace) {
        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§2Орк");

        List<String> lore = Arrays.asList(
                "- Нажмите чтобы выбрать"
        );

        meta.setLore(lore);
        item.setItemMeta(meta);
        return setHighlightedRace(item, currentRace.equals("ork"));
    }
}