package dev.elysium.eraces.datatypes;

import lombok.Data;

import java.nio.channels.InterruptedByTimeoutException;
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
    @RaceProperty(path = "at_block", type = FieldType.LIST_SUBGROUP)
    List<EffectsWithBlock> effectsWithBlocks = new ArrayList<>();
    @RaceProperty(path = "at_time", type = FieldType.LIST_SUBGROUP)
    List<EffectsWithTime> effectsWithTime = new ArrayList<>();
    @RaceProperty(path = "at_resurrection" , type = FieldType.MAP_STRING_INT)
    Map<String, Integer> effectsWithResurrection = new HashMap<String, Integer>();
    @RaceProperty(path = "in_water", type = FieldType.MAP_STRING_INT)
    Map<String, Integer> inWater = new HashMap<>();
}
