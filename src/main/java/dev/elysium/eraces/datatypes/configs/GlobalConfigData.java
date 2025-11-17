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
    public boolean isDebug;

    @ConfigsProperty(path = "plugin.lang", type = FieldType.STRING, defaultString = "ru")
    private String lang;

    @ConfigsProperty(path = "cast.timeout", type = FieldType.INT, defaultInt = 10000)
    private int castTimeoutMs;

    @ConfigsProperty(path = "cast.max-length", type = FieldType.INT, defaultInt = 20)
    private int castMaxLength;

    @ConfigsProperty(path = "cast.feedback", type = FieldType.BOOLEAN, defaultBoolean = true)
    private boolean castFeedback;

    public GlobalConfigData() {}
}
