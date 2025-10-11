package dev.elysium.eraces.datatypes.configs;

import dev.elysium.eraces.datatypes.FieldType;
import dev.elysium.eraces.datatypes.RaceProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecializationConfigData {
    @RaceProperty(path = "name", type = FieldType.STRING)
    String name = "";

    @RaceProperty(path = "STR", type = FieldType.DOUBLE)
    double strength = 25.0;

    @RaceProperty(path = "INT", type = FieldType.DOUBLE)
    double intelligent = 25.0;

    @RaceProperty(path = "AGI", type = FieldType.DOUBLE)
    double agility = 25.0;

    @RaceProperty(path = "VIT", type = FieldType.DOUBLE)
    double vitality = 25.0;
}
