package dev.elysium.eraces.datatypes.configs;

import dev.elysium.eraces.datatypes.FieldType;
import dev.elysium.eraces.datatypes.RaceProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SpecializationsConfigData {
    @RaceProperty(path = "xp_next", type = FieldType.STRING)
    String xpNextFormula = "xp_next = 100*pow(level, 1.5)+50*level";

    @RaceProperty(path = "points_per_level", type = FieldType.STRING)
    String pointsPerLevel = "points_per_level = 5+(level/10)";

    @RaceProperty(path = "spec", type = FieldType.LIST_SUBGROUP)
    List<SpecializationData> specializations = new ArrayList<>();
}
