package dev.elysium.eraces.datatypes

import lombok.Data

@Data
class EffectsWith {
    @RaceProperty(path = "global", type = FieldType.MAP_STRING_INT)
    var global: MutableMap<String, Int> = HashMap()

    @RaceProperty(path = "in_biome", type = FieldType.LIST_SUBGROUP)
    var effectsWithBiomes: MutableList<EffectsWithBiome> = ArrayList()

    @RaceProperty(path = "at_light", type = FieldType.LIST_SUBGROUP)
    var effectsWithLights: MutableList<EffectsWithLight> = ArrayList()

    @RaceProperty(path = "at_block", type = FieldType.LIST_SUBGROUP)
    var effectsWithBlocks: MutableList<EffectsWithBlock> = ArrayList()

    @RaceProperty(path = "at_time", type = FieldType.LIST_SUBGROUP)
    var effectsWithTime: MutableList<EffectsWithTime> = ArrayList()

    @RaceProperty(path = "at_resurrection", type = FieldType.MAP_STRING_INT)
    var effectsWithResurrection: MutableMap<String, Int> = HashMap()

    @RaceProperty(path = "in_water", type = FieldType.MAP_STRING_INT)
    var inWater: MutableMap<String, Int> = HashMap()

    @RaceProperty(path = "in_world", type = FieldType.LIST_SUBGROUP)
    var inWorld: MutableList<EffectsWithWorld> = ArrayList()
}
