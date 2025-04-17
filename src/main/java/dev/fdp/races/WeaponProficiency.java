package dev.fdp.races;

public class WeaponProficiency {
  private double bowDamageMultiplier = 1.0;
  private double bowProjectileSpeedMultiplier = 1.0;
  private double swordDamageMultiplier = 1.0;
  private double axeDamageMultiplier = 1.0;
  private double maceDamageMultiplier = 1.0;
  private double handDamageAdditional = 0.0;
  private double damageAdditional = 0.0;
  private double damageAdditionalWithIronAndLowerArmor = 0.0;
  private double dualWeaponDamageAdditional = 0.0;

  // Геттеры и сеттеры
  public double getBowDamageMultiplier() {
    return bowDamageMultiplier;
  }

  public void setBowDamageMultiplier(double bowDamageMultiplier) {
    this.bowDamageMultiplier = bowDamageMultiplier;
  }

    public double getDamageAdditionalWithIronAndLowerArmor() {
    return damageAdditionalWithIronAndLowerArmor;
  }

  public void setDamageAdditionalWithIronAndLowerArmor(double damageAdditionalWithIronAndLowerArmor) {
    this.damageAdditionalWithIronAndLowerArmor = damageAdditionalWithIronAndLowerArmor;
  }


  public double getBowProjectileSpeedMultiplier() {
    return bowProjectileSpeedMultiplier;
  }

  public void setBowProjectileSpeedMultiplier(double bowProjectileSpeedMultiplier) {
    this.bowProjectileSpeedMultiplier = bowProjectileSpeedMultiplier;
  }

  public double getSwordDamageMultiplier() {
    return swordDamageMultiplier;
  }

  public void setSwordDamageMultiplier(double swordDamageMultiplier) {
    this.swordDamageMultiplier = swordDamageMultiplier;
  }

  public double getAxeDamageMultiplier() {
    return axeDamageMultiplier;
  }

  public void setAxeDamageMultiplier(double axeDamageMultiplier) {
    this.axeDamageMultiplier = axeDamageMultiplier;
  }

  public double getMaceDamageMultiplier() {
    return maceDamageMultiplier;
  }

  public void setMaceDamageMultiplier(double maceDamageMultiplier) {
    this.maceDamageMultiplier = maceDamageMultiplier;
  }

  public double getHandDamageAdditional() {
    return handDamageAdditional;
  }

  public void setHandDamageAdditional(double handDamageAdditional) {
    this.handDamageAdditional = handDamageAdditional;
  }

  public double getDamageAdditional() {
    return damageAdditional;
  }

  public void setDamageAdditional(double damageAdditional) {
    this.damageAdditional = damageAdditional;
  }

  public double getDualWeaponDamageAdditional() {
    return dualWeaponDamageAdditional;
  }

  public void setDualWeaponDamageAdditional(double dualWeaponDamageAdditional) {
    this.dualWeaponDamageAdditional = dualWeaponDamageAdditional;
  }
}
