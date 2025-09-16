package dev.elysium.eraces.datatypes;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class EffectsWith {
    @RaceProperty(path = "global", type = FieldType.MAP_STRING_INT)
    Map<String, Integer> global = new HashMap<>();
}
