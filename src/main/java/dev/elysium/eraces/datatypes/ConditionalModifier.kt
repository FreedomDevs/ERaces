package dev.elysium.eraces.datatypes

class ConditionalModifier {
    @RaceProperty(path = "condition", type = FieldType.STRING)
    var condition: String = ""

    @RaceProperty(path = "multiplier", type = FieldType.DOUBLE)
    var multiplier: Double = 1.0
}
