package dev.elysium.eraces.datatypes

class ClumsinessChance {
    @RaceProperty(path = "chance", type = FieldType.DOUBLE)
    var chance: Double = 0.0

    @RaceProperty(path = "damage", type = FieldType.DOUBLE)
    var damage: Double = 1.0
}