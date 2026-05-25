package dev.elysium.eraces.datatypes;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class EffectsWithBlock {
    @RaceProperty(path = "blocks", type = FieldType.LIST)
    List<String> blocks = new ArrayList<>();

    @RaceProperty(path = "effects", type = FieldType.MAP_STRING_INT)
    Map<String, Integer> effects = new HashMap<>();
}
