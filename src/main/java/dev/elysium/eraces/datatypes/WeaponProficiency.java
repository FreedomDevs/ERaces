package dev.elysium.eraces.datatypes;

import lombok.Data;

@Data
public class WeaponProficiency {
    @RaceProperty(path = "bow_damage_multiplier", type = FieldType.DOUBLE)
    double bowDamageMultiplier = 1.0;
    @RaceProperty(path = "bow_projectile_speed_multiplier", type = FieldType.DOUBLE)
    double bowProjectileSpeedMultiplier = 1.0;
    @RaceProperty(path = "sword_damage_multiplier", type = FieldType.DOUBLE)
    double swordDamageMultiplier = 1.0;
    @RaceProperty(path = "axe_damage_multiplier", type = FieldType.DOUBLE)
    double axeDamageMultiplier = 1.0;
    @RaceProperty(path = "mace_damage_multiplier", type = FieldType.DOUBLE)
    double maceDamageMultiplier = 1.0;
    @RaceProperty(path = "hand_damage_additional", type = FieldType.DOUBLE)
    double handDamageAdditional = 0.0;
    @RaceProperty(path = "damage_additional", type = FieldType.DOUBLE)
    double damageAdditional = 0.0;
    @RaceProperty(path = "damage_additional_with_lower_than_iron_armor", type = FieldType.DOUBLE)
    double damageAdditionalWithIronAndLowerArmor = 0.0;
    @RaceProperty(path = "damage_additional_with_wolfs_near", type = FieldType.DOUBLE)
    double damageAdditionalWithWolfsNear = 0.0;
    @RaceProperty(path = "dual_weapon_damage_additional", type = FieldType.DOUBLE)
    double dualWeaponDamageAdditional = 0.0;
}
