package dev.elysium.eraces.datatypes;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class EffectsWithWorld {
    @RaceProperty(path = "world", type = FieldType.STRING)
    String world = "world";
    @RaceProperty(path = "effects", type = FieldType.MAP_STRING_INT)
    Map<String, Integer> effects = new HashMap<>();
}
