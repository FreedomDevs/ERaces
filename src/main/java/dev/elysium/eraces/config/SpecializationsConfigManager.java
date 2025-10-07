package dev.elysium.eraces.config;

import dev.elysium.eraces.datatypes.ReflectionUtils;
import dev.elysium.eraces.datatypes.configs.SpecializationData;
import dev.elysium.eraces.datatypes.configs.SpecializationsConfigData;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseMathLib;

import java.util.List;
import java.util.logging.Level;

public class SpecializationsConfigManager {
    private static final String FILE_NAME = "specializations.yml";
    private final Globals luaGlobals;

    @Getter
    private LuaValue xpNextFormula;
    @Getter
    private LuaValue pointsPerLevelFormula;
    @Getter
    List<SpecializationData> specializations;

    private final YamlManager cfgManager;
    private final JavaPlugin plugin;

    public SpecializationsConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.cfgManager = new YamlManager(this.plugin, FILE_NAME, true);
        this.luaGlobals = new Globals();

        luaGlobals.load(new JseBaseLib());
        luaGlobals.load(new Bit32Lib());
        luaGlobals.load(new TableLib());
        luaGlobals.load(new StringLib());
        luaGlobals.load(new JseMathLib());

        LoadState.install(luaGlobals);
        LuaC.install(luaGlobals);

        reloadConfig();
    }


    public void reloadConfig() {
        ConfigurationSection section = cfgManager.getConfig();

        SpecializationsConfigData specializationsConfigData = new SpecializationsConfigData();

        try {
            ReflectionUtils.loadSection(specializationsConfigData, section);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Произошла ошибка при загрузке specialization.yml", e);
        }

        this.xpNextFormula = luaGlobals.load(specializationsConfigData.getXpNextFormula());
        this.pointsPerLevelFormula = luaGlobals.load(specializationsConfigData.getPointsPerLevel());
        this.specializations = specializationsConfigData.getSpecializations();
    }
}