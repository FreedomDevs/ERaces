package dev.fdp.races;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class Race {
    private String id;
    private double maxHp = 20.0;
    private double hungerLossMultiplier = 1.0;
    private double mineSpeed = 1.0;
    private int handDistanceBonus = 0;
    private double additionalArmor = 0.0;
    private boolean shieldUsage = true;
    private double regenerationPerSec = 0.0;
    private int runningSpeed = 0;
    private int antiKnockbackLevel = 0;
    private int antiKnocbackLevelWithIronArmorAndMore = 0;
    private int slowdownLevel = 0;
    private int damageResistanceLevel = 0;
    private boolean peacefulMobsAfraid = false;
    private List<String> forbiddenFoods = new ArrayList<>();
    private List<String> biomeSpeed = new ArrayList<>();
    private WeaponProficiency weaponProficiency;
    private RaceGuiConfig raceGuiConfig;

    private List<String> visuals = new ArrayList<>();
}
