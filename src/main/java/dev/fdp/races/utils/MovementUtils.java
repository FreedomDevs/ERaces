package dev.fdp.races.utils;

import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.potion.PotionEffectType;

public class MovementUtils {
    private static final double SPEED_BASE_ATTRIBUTE = 0.1;

    public static double getSpeedAttributeValue(int effectLevel) {
        if (effectLevel == 0) return SPEED_BASE_ATTRIBUTE;
        if (effectLevel > 0)
            return SPEED_BASE_ATTRIBUTE * (1 + PotionEffectType.SPEED.getAttributeModifierAmount(Attribute.GENERIC_MOVEMENT_SPEED, effectLevel - 1));
        return SPEED_BASE_ATTRIBUTE * (1 + PotionEffectType.SLOWNESS.getAttributeModifierAmount(Attribute.GENERIC_MOVEMENT_SPEED, -effectLevel - 1));
    }

    public static void setSpeedAttributeLevel(Attributable entity, int effectLevel) {
        AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (attribute != null)
            attribute.setBaseValue(getSpeedAttributeValue(effectLevel));
    }
}
