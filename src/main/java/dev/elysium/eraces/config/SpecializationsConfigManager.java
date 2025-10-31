package dev.elysium.eraces.config;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.datatypes.ReflectionUtils;
import dev.elysium.eraces.datatypes.configs.SpecializationConfigData;
import dev.elysium.eraces.datatypes.configs.SpecializationData;
import dev.elysium.eraces.datatypes.configs.SpecializationsConfigData;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.luaj.vm2.*;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class SpecializationsConfigManager {
    private static final String FILE_NAME = "specializations.yml";
    private final Globals luaGlobals;

    @Getter
    private LuaValue xpNextFormula;
    @Getter
    private LuaValue pointsPerLevelFormula;
    @Getter
    HashMap<String, SpecializationData> specializations;

    private final YamlManager cfgManager;
    private final JavaPlugin plugin;

    public SpecializationsConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.cfgManager = new YamlManager(this.plugin, FILE_NAME, true);
        //this.luaGlobals = JsePlatform.standardGlobals();

        this.luaGlobals = new Globals();
        luaGlobals.load(new JseBaseLib());
        luaGlobals.load(new PackageLib());
        luaGlobals.load(new Bit32Lib());
        luaGlobals.load(new TableLib());
        luaGlobals.load(new StringLib());
        //luaGlobals.load(new CoroutineLib());
        luaGlobals.load(new JseMathLib());
        //luaGlobals.load(new JseIoLib());
        //luaGlobals.load(new JseOsLib());
        //luaGlobals.load(new LuajavaLib());
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

        this.specializations = new HashMap<>();
        for (SpecializationConfigData i : specializationsConfigData.getSpecializations()) {
            SpecializationData data = new SpecializationData();
            data.setStrength(data.getStrength());
            data.setIntelligent(data.getIntelligent());
            data.setAgility(data.getAgility());
            data.setVitality(data.getVitality());
            this.specializations.put(i.getName(), data);
        }

        this.specializations.put("", new SpecializationData());

        pointsPerLevelCache.clear();
        xpNextCache.clear();
    }

    private final Map<Long, Long> xpNextCache = new HashMap<>();
    public long getXpNext(long level) {
        return xpNextCache.computeIfAbsent(level, this::calculateXpNext);
    }
    private long calculateXpNext(long level) {
        luaGlobals.set("level", LuaValue.valueOf(level));

        try {
            LuaValue result = xpNextFormula.call();
            long xpNext = result.checklong();
            if (xpNext < 1) {
                ERaces.getInstance().getLogger().warning("При вычислении xpNext для уровня " + level + " было получено отрицательное число или 0(засчитано как 1)");
                return 1;
            }

            return xpNext;
        } catch (LuaError error) {
            ERaces.getInstance().getLogger().log(Level.SEVERE, "Не удалось расчитать xpNext для уровня " + level, error);
            return Long.MAX_VALUE;
        } finally {
            luaGlobals.set("level", LuaValue.NIL);
        }
    }

    private final Map<Long, Long> pointsPerLevelCache = new HashMap<>();
    public long getPointsPerLevel(long level) {
        return pointsPerLevelCache.computeIfAbsent(level, this::calculatePointsPerLevel);
    }
    private long calculatePointsPerLevel(long level) {
        luaGlobals.set("level", LuaValue.valueOf(level));

        try {
            LuaValue result = pointsPerLevelFormula.call();
            long pointsPerLevel = result.checklong();
            if (pointsPerLevel < 1) {
                ERaces.getInstance().getLogger().warning("При вычислении pointsPerLevel для уровня " + level + " было получено отрицательное число или 0(засчитано как 1)");
                return 1;
            }

            return pointsPerLevel;
        } catch (LuaError error) {
            ERaces.getInstance().getLogger().log(Level.SEVERE, "Не удалось расчитать pointsPerLevel для уровня " + level, error);
            return Long.MAX_VALUE;
        } finally {
            luaGlobals.set("level", LuaValue.NIL);
        }
    }
}