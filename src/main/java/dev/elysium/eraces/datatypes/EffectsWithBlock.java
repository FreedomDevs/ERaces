package dev.elysium.eraces.datatypes;

import lombok.Data;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class EffectsWithBlock {
    @RaceProperty(path = "blocks", type = FieldType.LIST)
    List<Material> blocks = new ArrayList<>();

    @RaceProperty(path = "effects", type = FieldType.MAP_STRING_INT)
    Map<String, Integer> effects = new HashMap<>();
}
