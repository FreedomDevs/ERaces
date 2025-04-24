package dev.fdp.races;

import lombok.Data;

@Data
public class WeaponProficiency {
  private double bowDamageMultiplier = 1.0;
  private double bowProjectileSpeedMultiplier = 1.0;
  private double swordDamageMultiplier = 1.0;
  private double axeDamageMultiplier = 1.0;
  private double maceDamageMultiplier = 1.0;
  private double handDamageAdditional = 0.0;
  private double damageAdditional = 0.0;
  private double damageAdditionalWithIronAndLowerArmor = 0.0;
  private double damageAdditionalWithWolfsNear = 0.0;
  private double dualWeaponDamageAdditional = 0.0;
}
