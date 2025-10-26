package dev.elysium.eraces.datatypes

class EffectsWithResurrection {
    @RaceProperty(path = "effects", type = FieldType.MAP_STRING_INT)
    var effects: MutableMap<String?, Int?> = HashMap<String?, Int?>()
}