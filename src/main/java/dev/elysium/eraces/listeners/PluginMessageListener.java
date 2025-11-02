package dev.elysium.eraces.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.abilities.AbilsManager;
import dev.elysium.eraces.datatypes.configs.GlobalConfigData;
import dev.elysium.eraces.utils.ChatUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PluginMessageListener implements org.bukkit.plugin.messaging.PluginMessageListener {

    private static final GlobalConfigData cfg = ERaces.getInstance()
            .getContext()
            .getGlobalConfigManager()
            .getData();

    private static class CastingSession {
        final StringBuilder keys = new StringBuilder();
        volatile long lastUpdateMillis = System.currentTimeMillis();

        void appendKey(String k) {
            keys.append(k);
            lastUpdateMillis = System.currentTimeMillis();
        }

        String getKeys() {
            return keys.toString();
        }

        boolean isExpired() {
            return System.currentTimeMillis() - lastUpdateMillis > cfg.getCastTimeoutMs();
        }
    }

    private final Map<UUID, CastingSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if (!channel.equals("elysium:eraces_cast")) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);

        long timestamp;
        String type;
        String payload;
        try {
            timestamp = in.readLong();
            type = in.readUTF();
            payload = in.readUTF();
        } catch (Exception e) {
            ERaces.getInstance().getLogger().warning("Ошибка парсинга пакета eraces_cast от " + player.getName());
            return;
        }

        String t = type.toLowerCase();

        CastingSession session = sessions.get(player.getUniqueId());
        if (session != null && session.isExpired()) {
            sessions.remove(player.getUniqueId());
            ERaces.getInstance().getLogger().fine("Сессия кастинга игрока " + player.getName() + " устарела и была удалена.");
        }

        switch (t) {
            case "start_cast" -> handleStart(player);
            case "cast_key" -> handleKey(player, payload);
            case "end_cast" -> handleEnd(player, payload);
            default -> ERaces.getInstance().getLogger().warning("Неизвестный тип пакета eraces_cast: " + type);
        }
    }

    private void handleStart(Player player) {
        CastingSession s = new CastingSession();
        sessions.put(player.getUniqueId(), s);
        ERaces.getInstance().getLogger().fine("Игрок " + player.getName() + " начал ввод комбо.");

        if (cfg.isCastFeedback()) {
            ChatUtil.INSTANCE.sendActionOld(player, "<green>Начат ввод комбинации...");
        }
    }

    private void handleKey(Player player, String payload) {
        CastingSession s = sessions.get(player.getUniqueId());
        if (s == null) {
            ERaces.getInstance().getLogger().fine("Игрок " + player.getName() + " отправил cast_key без start_cast.");
            if (cfg.isCastFeedback()) ChatUtil.INSTANCE.sendActionOld(player, "<red>Сначала начни ввод (start cast).");
            return;
        }

        if (s.isExpired()) {
            sessions.remove(player.getUniqueId());
            if (cfg.isCastFeedback()) ChatUtil.INSTANCE.sendActionOld(player, "<red>Время ввода прошло — начни заново.");
            return;
        }

        if (s.getKeys().length() >= cfg.getCastMaxLength()) {
            if (cfg.isCastFeedback()) ChatUtil.INSTANCE.sendActionOld(player, "<red>Комбинация слишком длинная.");
            return;
        }

        s.appendKey(payload);
        ERaces.getInstance().getLogger().fine("Игрок " + player.getName() + " добавил клавишу: " + payload + " (текущая: " + s.getKeys() + ")");
        if (cfg.isCastFeedback()) {
            ChatUtil.INSTANCE.sendActionOld(player, "<yellow>Текущая комбинация: <white>" + s.getKeys());
        }
    }

    private void handleEnd(Player player, String payload) {
        CastingSession s = sessions.get(player.getUniqueId());
        if (s == null) {
            if (cfg.isCastFeedback()) ChatUtil.INSTANCE.sendActionOld(player, "<red>Нечего завершать — начни ввод комбинации сначала.");
            ERaces.getInstance().getLogger().fine("Игрок " + player.getName() + " прислал end_cast без активной сессии.");
            return;
        }

        if (s.isExpired()) {
            sessions.remove(player.getUniqueId());
            if (cfg.isCastFeedback()) ChatUtil.INSTANCE.sendActionOld(player, "<red>Время ввода истекло — комбинация сброшена.");
            return;
        }

        String collected = s.getKeys();
        sessions.remove(player.getUniqueId());

        if (collected.isEmpty()) {
            if (cfg.isCastFeedback()) ChatUtil.INSTANCE.sendActionOld(player, "<red>Комбинация пуста.");
            return;
        }

        ERaces.getInstance().getLogger().info("Игрок " + player.getName() + " ввёл комбинацию: " + collected);
        try {
            AbilsManager.getInstance().activateByCombo(player, collected);
        } catch (Exception e) {
            ERaces.getInstance().getLogger().severe("Ошибка при активации комбинации '" + collected + "' у игрока " + player.getName());
            e.printStackTrace();
            if (cfg.isCastFeedback()) ChatUtil.INSTANCE.sendActionOld(player, "<red>Ошибка при активации способности.");
        }
    }
}
