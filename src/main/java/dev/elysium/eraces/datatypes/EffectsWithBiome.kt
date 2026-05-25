package dev.elysium.eraces.datatypes

import lombok.Data

@Data
class EffectsWithBiome {
    @RaceProperty(path = "biomes", type = FieldType.LIST)
    var biomes: MutableList<String> = ArrayList()

    @RaceProperty(path = "effects", type = FieldType.MAP_STRING_INT)
    var effects: MutableMap<String, Int> = HashMap()
}
