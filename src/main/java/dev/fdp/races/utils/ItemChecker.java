package dev.fdp.races.utils;

import org.bukkit.Material;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class ItemChecker {
    private interface IMatch {
        boolean matches(Material material);
    }

    private enum ToolSuffixes implements IMatch {
        SWORD("SWORD"),
        PICKAXE("PICKAXE"),
        AXE("AXE"),
        HOE("HOE"),
        SHOVEL("SHOVEL");
        private final String suffix;

        ToolSuffixes(String suffix) {
            this.suffix = suffix;
        }

        @Override
        public boolean matches(Material material) {
            return material.toString().endsWith(suffix);
        }
    }

    private enum ToolMaterial implements IMatch {
        TRIDENT(Material.TRIDENT),
        MACE(Material.MACE);
        private final Material material;

        ToolMaterial(Material material) {
            this.material = material;
        }

        @Override
        public boolean matches(Material material) {
            return this.material == material;
        }
    }

    public enum ToolTypes {
        SWORD(ToolSuffixes.SWORD),
        PICKAXE(ToolSuffixes.PICKAXE),
        AXE(ToolSuffixes.AXE),
        HOE(ToolSuffixes.HOE),
        SHOVEL(ToolSuffixes.SHOVEL),
        TRIDENT(ToolMaterial.TRIDENT),
        MACE(ToolMaterial.MACE);
        private final IMatch type;

        ToolTypes(IMatch t) {
            type = t;
        }

        public boolean matches(Material material) {
            return this.type.matches(material);
        }

        public static Set<ToolTypes> getTools() {
            return new HashSet<>(EnumSet.allOf(ToolTypes.class));
        }
    }


    public static boolean isTool(Material mat) {
        return ToolTypes.getTools().stream().anyMatch(t -> t.matches(mat));
    }

    public static boolean isToolType(Material mat, ToolTypes type) {
        return type.matches(mat);
    }
}
