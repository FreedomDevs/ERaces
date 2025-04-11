package dev.fdp.races;

public class WeaponProficiency {
  private double bowDamageMultiplier = 1.0;
  private double bowProjectileSpeedMultiplier = 1.0;
  private double swordDamageMultiplier = 1.0;
  private double axeDamageMultiplier = 1.0;
  private double maceDamageMultiplier = 1.0;
  private double handDamageAdditional = 0.0;
  private double dualWeaponDamageAdditional = 0.0;

  // Геттеры и сеттеры
  public double getBowDamageMultiplier() {
    return bowDamageMultiplier;
  }

  public void setBowDamageMultiplier(double bowDamageMultiplier) {
    this.bowDamageMultiplier = bowDamageMultiplier;
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

  public double getDualWeaponDamageAdditional() {
    return dualWeaponDamageAdditional;
  }

  public void setDualWeaponDamageAdditional(double dualWeaponDamageAdditional) {
    this.dualWeaponDamageAdditional = dualWeaponDamageAdditional;
  }

  @Override
  public String toString() {
    return "WeaponProficiency{" +
        "bowDamageMultiplier=" + bowDamageMultiplier +
        ", bowProjectileSpeedMultiplier=" + bowProjectileSpeedMultiplier +
        ", swordDamageMultiplier=" + swordDamageMultiplier +
        ", axeDamageMultiplier=" + axeDamageMultiplier +
        ", maceDamageMultiplier=" + maceDamageMultiplier +
        ", handDamageAdditional=" + handDamageAdditional +
        ", dualWeaponDamageAdditional=" + dualWeaponDamageAdditional +
        '}';
  }
}
