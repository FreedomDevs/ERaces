package dev.elysium.eraces.datatypes.configs;

import dev.elysium.eraces.datatypes.FieldType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalConfigData {

    @ConfigsProperty(path = "plugin.prefix", type = FieldType.STRING, defaultString = "&6[ERaces] ")
    private String prefix;

    @ConfigsProperty(path = "plugin.debug", type = FieldType.BOOLEAN, defaultBoolean = false)
    private boolean debug;

    @ConfigsProperty(path = "plugin.lang", type = FieldType.STRING, defaultString = "ru")
    private String lang;

    public GlobalConfigData() {}
}
