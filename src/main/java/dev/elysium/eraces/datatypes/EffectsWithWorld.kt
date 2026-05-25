package dev.elysium.eraces.datatypes

import lombok.Data

@Data
class EffectsWithWorld {
    @RaceProperty(path = "world", type = FieldType.STRING)
    var world: String = "world"

    @RaceProperty(path = "effects", type = FieldType.MAP_STRING_INT)
    var effects: MutableMap<String, Int> = HashMap()
}
