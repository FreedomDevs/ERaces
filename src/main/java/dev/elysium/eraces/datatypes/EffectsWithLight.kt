package dev.elysium.eraces.datatypes

import lombok.Data

@Data
class EffectsWithLight {
    @RaceProperty(path = "light_type", type = FieldType.STRING)
    var lightType: String = "sum"

    @RaceProperty(path = "min", type = FieldType.INT)
    var minLight: Int = 0

    @RaceProperty(path = "max", type = FieldType.INT)
    var maxLight: Int = 0

    @RaceProperty(path = "effects", type = FieldType.MAP_STRING_INT)
    var effects: MutableMap<String, Int> = HashMap()
}
