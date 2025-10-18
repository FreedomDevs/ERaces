package dev.elysium.eraces.datatypes

class EffectsWithTime {
    @RaceProperty(path = "from", type = FieldType.INT)
    var from: Int = 12000

    @RaceProperty(path = "tp", type = FieldType.INT)
    var to: Int = 24000

    @RaceProperty(path = "effects", type = FieldType.MAP_STRING_INT)
    var effects: MutableMap<String, Int> = hashMapOf()
}