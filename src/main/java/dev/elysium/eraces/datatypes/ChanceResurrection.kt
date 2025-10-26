package dev.elysium.eraces.datatypes

class ChanceResurrection {
    @RaceProperty(path = "chance", type = FieldType.DOUBLE)
    var chance: Double = 0.0

    @RaceProperty(path = "multiplier", type = FieldType.DOUBLE)
    var nexHpMinus: Double = 0.0
}