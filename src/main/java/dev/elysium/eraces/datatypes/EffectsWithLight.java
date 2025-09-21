package dev.elysium.eraces.datatypes;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class EffectsWithLight {
    @RaceProperty(path = "light_type", type = FieldType.STRING)
    String lightType = "sum";
    @RaceProperty(path = "min", type = FieldType.INT)
    Integer minLight = 0;
    @RaceProperty(path = "max", type = FieldType.INT)
    Integer maxLight = 0;
    @RaceProperty(path = "effects", type = FieldType.MAP_STRING_INT)
    Map<String, Integer> effects = new HashMap<>();
}
