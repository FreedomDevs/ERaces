package dev.elysium.eraces.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Arrays;

public class ArmorChecker {
    public enum ArmorType {
        LEATHER,
        CHAINMAIL,
        GOLDEN,
        IRON,
        DIAMOND,
        NETHERITE;


        @Nullable
        public static ArmorType getType(Material mat) {
            ArmorType res = null;
            for (ArmorType t : ArmorType.values()) {
                if (mat.toString().contains(t.name())) {
                    res = t;
                    break;
                }
            }
            return res;
        }
    }

    public static boolean isGEQ(@Nullable Material mat, ArmorType type) {
        if (mat == null) return false;
        if (!mat.getEquipmentSlot().isArmor()) return false;
        ArmorType t = ArmorType.getType(mat);
        if (t == null) return false;
        return t.ordinal() >= type.ordinal();
    }

    public static boolean allArmorGEQ(Player player, ArmorType type) {
        return Arrays.stream(player.getInventory().getArmorContents()).allMatch(t -> t != null && ArmorChecker.isGEQ(t.getType(), type));
    }

    public static boolean allArmorLess(Player player, ArmorType type) {
        return Arrays.stream(player.getInventory().getArmorContents()).allMatch(t -> t == null || !ArmorChecker.isGEQ(t.getType(), type));
    }
}
