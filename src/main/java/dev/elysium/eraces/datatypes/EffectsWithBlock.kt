package dev.elysium.eraces.datatypes

import lombok.Data

@Data
class EffectsWithBlock {
    @RaceProperty(path = "blocks", type = FieldType.LIST)
    var blocks: MutableList<String> = ArrayList()

    @RaceProperty(path = "effects", type = FieldType.MAP_STRING_INT)
    var effects: MutableMap<String, Int> = HashMap()
}
