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
                "§aЛесной народ, в гармонии с природой.",
                "",
                "§eБонусы:",
                "§7»  Скорость II в лесах и равнинах.",
                "§7»  Удвоенный урон и скорость стрел из лука.",
                "§7»  Метаболизм: расход голода на 25% меньше.",
                "",
                "§cМинусы:",
                "§7»  Только 15 HP (7.5 сердечек).",
                "§7»  Штраф за ношение брони выше железной — замедление I.",
                "§7»  Не может есть мясо (вегетарианец).",
                "§7»  Урон ближнего боя x0.5.",
                "",
                "§7Для выбора нажмите: §f[ПКМ]"
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
                "§eСильны духом, ловки руками. Прирождённые шахтёры.",
                "",
                "§eБонусы:",
                "§7»  Ускоренная добыча блоков (x1.5).",
                "§7»  Увеличенная досягаемость (+1 блок).",
                "§7",
                "§cМинусы: отсутствуют.",
                "",
                "§7Для выбора нажмите: §f[ПКМ]"
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
                "§6Медвежья выносливость и грубая сила.",
                "",
                "§eБонусы:",
                "§7»  30 HP.",
                "§7»  3 единицы брони.",
                "§7»  Иммунитет к отбрасыванию.",
                "§7»  Урон булавой x1.5.",
                "",
                "§cМинусы:",
                "§7»  Урон луком x0.5, мечом x0.75.",
                "§7»  Не может использовать щит.",
                "",
                "§7Для выбора нажмите: §f[ПКМ]"
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
                "§7Сильнее в стае, быстр и вынослив.",
                "",
                "§eБонусы:",
                "§7»  +3 урона когтями.",
                "§7»  Регенерация +0.25 HP/сек.",
                "§7»  +2 урона при наличии рядом союзников-волков (или собак).",
                "§7»  Скорость I.",
                "§7»  +1 броня от шкуры.",
                "",
                "§cМинусы:",
                "§7»  Только 20 HP.",
                "§7»  Не может использовать щит.",
                "§7»  Мирные мобы избегают волков.",
                "",
                "§7Для выбора нажмите: §f[ПКМ]"
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
                "§6Быстрый, сильный, но дик.",
                "",
                "§eБонусы:",
                "§7»  +6 урона без оружия.",
                "§7»  Постоянная скорость II.",
                "§7»  25 HP и 2 брони.",
                "",
                "§cМинусы:",
                "§7»  Не может использовать ближнее оружие (топор x0.5, лук x0.75).",
                "§7»  Не может использовать щит.",
                "",
                "§7Для выбора нажмите: §f[ПКМ]"
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
                "§2Грубая сила и дикая ярость.",
                "",
                "§eБонусы:",
                "§7»  30 HP.",
                "§7»  Сопротивление урону I и отбрасыванию при железной броне и выше.",
                "§7»  +2 урона при парном оружии.",
                "§7",
                "§cМинусы:",
                "§7»  Если броня ниже железной — весь получаемый урон x1.75.",
                "§7»  При использовании щита — замедление I.",
                "§7»  Всегда замедление I (неповоротливость).",
                "",
                "§7Для выбора нажмите: §f[ПКМ]"
        );

        meta.setLore(lore);
        item.setItemMeta(meta);
        return setHighlightedRace(item, currentRace.equals("ork"));
    }
}