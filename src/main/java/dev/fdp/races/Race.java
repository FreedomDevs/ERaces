package dev.fdp.races;

import java.util.ArrayList;
import java.util.List;

public class Race {
  private String id;
  private String name;
  private double maxHp = 20.0;
  private double hungerLossMultiplier = 1.0;
  private double mineSpeed = 1.0;
  private int handDistanceBonus = 0;
  private double additionalArmor = 0.0;
  private boolean shieldUsage = true;
  private double regenerationPerSec = 0.0;
  private int runningSpeed = 0;
  private int antiKnocbackLevel = 1;
  private int slowdownLevel = 0;
  private boolean peacefulMobsAfraid = false;
  private List<String> forbiddenFoods = new ArrayList<>();
  private List<String> biomeSpeed = new ArrayList<>();
  private WeaponProficiency weaponProficiency;
  private RaceGuiConfig raceGuiConfig;

  // Геттеры и сеттеры
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getSlowdownLevel() {
    return slowdownLevel;
  }

  public void setSlowdownLevel(int slowdownLevel) {
    this.slowdownLevel = slowdownLevel;
  }

  public List<String> getBiomeSpeed() {
    return biomeSpeed;
  }

  public void setBiomeSpeed(List<String> biomeSpeed) {
    this.biomeSpeed = biomeSpeed;
  }

  public void setRunningSpeed(int runningSpeed) {
    this.runningSpeed = runningSpeed;
  }

  public int getRunningSpeed() {
    return runningSpeed;
  }

  public void setAntiKnockbackLevel(int antiKnocbackLevel) {
    this.antiKnocbackLevel = antiKnocbackLevel;
  }

  public int getAntiKnockbackLevel() {
    return antiKnocbackLevel;
  }

  public double getMaxHp() {
    return maxHp;
  }

  public void setMaxHp(double maxHp) {
    this.maxHp = maxHp;
  }

  public double getHungerLossMultiplier() {
    return hungerLossMultiplier;
  }

  public void setHungerLossMultiplier(double hungerLossMultiplier) {
    this.hungerLossMultiplier = hungerLossMultiplier;
  }

  public double getMineSpeed() {
    return mineSpeed;
  }

  public void setMineSpeed(double mineSpeed) {
    this.mineSpeed = mineSpeed;
  }

  public int getHandDistanceBonus() {
    return handDistanceBonus;
  }

  public void setHandDistanceBonus(int handDistanceBonus) {
    this.handDistanceBonus = handDistanceBonus;
  }

  public double getAdditionalArmor() {
    return additionalArmor;
  }

  public void setAdditionalArmor(double additionalArmor) {
    this.additionalArmor = additionalArmor;
  }

  public boolean isShieldUsage() {
    return shieldUsage;
  }

  public void setShieldUsage(boolean shieldUsage) {
    this.shieldUsage = shieldUsage;
  }

  public double getRegenerationPerSec() {
    return regenerationPerSec;
  }

  public void setRegenerationPerSec(double regenerationPerSec) {
    this.regenerationPerSec = regenerationPerSec;
  }

  public List<String> getForbiddenFoods() {
    return forbiddenFoods;
  }

  public void setForbiddenFoods(List<String> forbiddenFoods) {
    this.forbiddenFoods = forbiddenFoods;
  }

  public WeaponProficiency getWeaponProficiency() {
    return weaponProficiency;
  }

  public void setWeaponProficiency(WeaponProficiency weaponProficiency) {
    this.weaponProficiency = weaponProficiency;
  }

  public RaceGuiConfig getRaceGuiConfig() {
    return raceGuiConfig;
  }

  public void setRaceGuiConfig(RaceGuiConfig raceGuiConfig) {
    this.raceGuiConfig = raceGuiConfig;
  }

  public boolean isPeacefulMobsAfraid() {
    return peacefulMobsAfraid;
  }

  public void setPeacefulMobsAfraid(boolean peacefulMobsAfraid) {
    this.peacefulMobsAfraid = peacefulMobsAfraid;
  }

  @Override
  public String toString() {
    return "Race{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", maxHp=" + maxHp +
        ", hungerLossMultiplier=" + hungerLossMultiplier +
        ", mineSpeed=" + mineSpeed +
        ", handDistanceBonus=" + handDistanceBonus +
        ", additionalArmor=" + additionalArmor +
        ", shieldUsage=" + shieldUsage +
        ", regenerationPerSec=" + regenerationPerSec +
        ", runningSpeed=" + runningSpeed +
        ", antiKnocbackLevel=" + antiKnocbackLevel +
        ", forbiddenFoods=" + forbiddenFoods +
        ", isPeacefulMobsAfraid=" + peacefulMobsAfraid +
        ", weaponProficiency=" + weaponProficiency +
        ", raceGuiConfig=" + raceGuiConfig +
        '}';
  }

}
