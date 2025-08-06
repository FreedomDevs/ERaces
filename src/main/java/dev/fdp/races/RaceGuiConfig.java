package dev.fdp.races;

import lombok.Data;

@Data
public class RaceGuiConfig {
    private String name = "";

    @Override
    public String toString() {
        return "RaceGuiConfig{" +
                "name=" + name +
                '}';
    }
}
