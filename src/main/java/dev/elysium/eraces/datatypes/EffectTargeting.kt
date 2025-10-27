package dev.elysium.eraces.datatypes

class EffectTargeting {
    @RaceProperty(path = "effects_on_player_targeted", type = FieldType.MAP_STRING_INT)
    var effectsOnPlayerTargeted: MutableMap<String, Int> = hashMapOf()
    @RaceProperty(path = "effects_on_player_targeted_time", type = FieldType.STRING)
    var effectsOnPlayerTargetedTime: String = "10s"

    @RaceProperty(path = "effects_on_player_attacking", type = FieldType.MAP_STRING_INT)
    var effectsOnPlayerAttacking: MutableMap<String, Int> = hashMapOf()
    @RaceProperty(path = "effects_on_player_attacking_time", type = FieldType.STRING)
    var effectsOnPlayerAttackingTime: String = "10s"
}