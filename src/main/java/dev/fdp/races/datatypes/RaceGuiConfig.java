package dev.fdp.races.datatypes;

import lombok.Data;

@Data
public class RaceGuiConfig {
    @RaceProperty(path = "name", type = FieldType.STRING)
    String name = "name_undefined";

    @Override
    public String toString() {
        return "RaceGuiConfig{" +
                "name=" + name +
                '}';
    }
}
