package dev.elysium.eraces.datatypes

class EffectTargeting {
    @RaceProperty(path = "effectsOnPlayerTargeted", type = FieldType.MAP_STRING_INT)
    var effectsOnPlayerTargeted: MutableMap<String, Int> = hashMapOf()
    @RaceProperty(path = "effectsOnPlayerTargeted_time", type = FieldType.STRING)
    var effectsOnPlayerTargetedTime: String = "10s"

    @RaceProperty(path = "effectsOnPlayerAttacking", type = FieldType.MAP_STRING_INT)
    var effectsOnPlayerAttacking: MutableMap<String, Int> = hashMapOf()
    @RaceProperty(path = "effectsOnPlayerAttacking_time", type = FieldType.STRING)
    var effectsOnPlayerAttackingTime: String = "10s"
}