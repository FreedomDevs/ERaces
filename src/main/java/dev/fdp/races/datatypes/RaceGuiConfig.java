package dev.fdp.races.datatypes;

import lombok.Data;

@Data
public class RaceGuiConfig {
    @RaceProperty(path = "name")
    String name = "name_undefined";
    @RaceProperty(path = "lore")
    String lore = "";
    @RaceProperty(path = "icon")
    String icon = "DIRT";
}
