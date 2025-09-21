package dev.elysium.eraces.datatypes;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class EffectsWith {
    @RaceProperty(path = "global", type = FieldType.MAP_STRING_INT)
    Map<String, Integer> global = new HashMap<>();
    @RaceProperty(path = "in_biome", type = FieldType.LIST_SUBGROUP)
    List<EffectsWithBiome> effectsWithBiomes = new ArrayList<>();
    @RaceProperty(path = "at_light", type = FieldType.LIST_SUBGROUP)
    List<EffectsWithLight> effectsWithLights = new ArrayList<>();
}
