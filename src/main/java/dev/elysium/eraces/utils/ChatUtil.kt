package dev.elysium.eraces.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtil {
    private static final MiniMessage miniMessage = MiniMessage.builder().tags(TagResolver.builder()
            .resolver(StandardTags.color())
            .resolver(StandardTags.gradient())
            .resolver(StandardTags.rainbow())
            .build()).build();

    private static final Pattern HEX_PATTERN = Pattern.compile("(?i)<#([0-9A-F]{6})>");

    /**
     * Форматирует строку с использованием MiniMessage и подставляет аргументы.
     *
     * @param text исходный текст с тегами MiniMessage
     * @param args карта аргументов, где ключ заменяется на значение в тексте
     * @return объект Component с применённым форматированием
     */
    public static Component format(String text, Map<String, String> args) {
        return miniMessage.deserialize(applyArgs(text, args));
    }

    /**
     * Форматирует строку с использованием MiniMessage без аргументов.
     *
     * @param text исходный текст с тегами MiniMessage
     * @return объект Component с применённым форматированием
     */
    public static Component format(String text) {
        return format(text, Collections.emptyMap());
    }

    /**
     * Форматирует строку в стиле legacy (старые цветовые коды Minecraft) с подстановкой аргументов.
     *
     * @param text исходный текст с цветами и аргументами
     * @param args карта аргументов
     * @return объект Component с применённым legacy форматированием
     */
    public static Component legacyFormat(String text, Map<String, String> args) {
        String colored = applyArgs(text, args);
        colored = hexToLegacy(colored).replace('&', '§');
        return clearItalic(LegacyComponentSerializer.legacySection().deserialize(colored));
    }

    /**
     * Форматирует строку в стиле legacy без аргументов.
     *
     * @param text исходный текст
     * @return объект Component с применённым legacy форматированием
     */
    public static Component legacyFormat(String text) {
        return legacyFormat(text, Collections.emptyMap());
    }

    /**
     * Создаёт компонент текста с указанным цветом.
     *
     * @param s текст
     * @param clr цвет (TextColor)
     * @return объект Component без наклонного текста
     */
    public static Component text(String s, TextColor clr) {
        return clearItalic(Component.text(s, clr));
    }

    /**
     * Создаёт компонент текста с цветом в hex формате.
     *
     * @param s текст
     * @param hex цвет в формате "#RRGGBB"
     * @return объект Component без наклонного текста
     */
    public static Component text(String s, String hex) {
        return clearItalic(Component.text(s, TextColor.fromHexString(hex)));
    }

    /**
     * Убирает наклонный стиль текста у компонента.
     *
     * @param text компонент текста
     * @return новый компонент без наклона
     */
    public static Component clearItalic(Component text) {
        return text.decoration(TextDecoration.ITALIC, false);
    }

    /**
     * Подставляет аргументы в текст (ключи заменяются значениями).
     *
     * @param text исходный текст
     * @param args карта аргументов
     * @return текст с подставленными значениями
     */
    public static String applyArgs(String text, Map<String, String> args) {
        String result = text;
        for (Map.Entry<String, String> entry : args.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * Отправляет форматированное сообщение игроку или консоли с аргументами.
     *
     * @param sender получатель сообщения (Player или CommandSender)
     * @param msg текст сообщения
     * @param args карта аргументов
     */
    public static void message(CommandSender sender, String msg, Map<String, String> args) {
        sender.sendMessage(format(msg, args));
    }

    /**
     * Отправляет форматированное сообщение без аргументов.
     *
     * @param sender получатель сообщения (Player или CommandSender)
     * @param msg текст сообщения
     */
    public static void message(CommandSender sender, String msg) {
        message(sender, msg, Collections.emptyMap());
    }

    /**
     * Отправляет legacy-сообщение с аргументами.
     *
     * @param sender получатель сообщения (Player или CommandSender)
     * @param msg текст сообщения
     * @param args карта аргументов
     */
    public static void legacyMessage(CommandSender sender, String msg, Map<String, String> args) {
        sender.sendMessage(legacyFormat(msg, args));
    }

    /**
     * Отправляет legacy-сообщение без аргументов.
     *
     * @param sender получатель сообщения (Player или CommandSender)
     * @param msg текст сообщения
     */
    public static void legacyMessage(CommandSender sender, String msg) {
        legacyMessage(sender, msg, Collections.emptyMap());
    }

    /**
     * Отправляет заголовок (title) и подзаголовок игроку с кастомными временами отображения.
     *
     * @param player игрок
     * @param title заголовок
     * @param subtitle подзаголовок
     * @param fadeIn время появления (в миллисекундах)
     * @param stay время отображения (в миллисекундах)
     * @param fadeOut время исчезновения (в миллисекундах)
     */
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.showTitle(Title.title(
                format(title),
                format(subtitle),
                Title.Times.times(
                        Duration.ofMillis(fadeIn),
                        Duration.ofMillis(stay),
                        Duration.ofMillis(fadeOut))));
    }

    /**
     * Отправляет заголовок и подзаголовок игроку с дефолтными временами отображения.
     *
     * @param player игрок
     * @param title заголовок
     * @param subtitle подзаголовок
     */
    public static void sendTitle(Player player, String title, String subtitle) {
        sendTitle(player, title, subtitle, 10, 20, 10);
    }

    /**
     * Отправляет сообщение в action bar игроку с аргументами.
     *
     * @param player игрок
     * @param msg текст сообщения
     * @param args карта аргументов
     */
    public static void sendAction(Player player, String msg, Map<String, String> args) {
        Component formatted = format(msg, args);
        player.sendActionBar(formatted);
    }

    /**
     * Отправляет сообщение в action bar игроку без аргументов.
     *
     * @param player игрок
     * @param msg текст сообщения
     */
    public static void sendAction(Player player, String msg) {
        sendAction(player, msg, Collections.emptyMap());
    }

    private static String hexToLegacy(String text) {
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group(1);
            StringBuilder legacy = new StringBuilder("§x");
            for (char c : hex.toCharArray()) {
                legacy.append('§').append(c);
            }
            matcher.appendReplacement(buffer, legacy.toString());
        }

        matcher.appendTail(buffer);
        return buffer.toString().replaceAll("</#([0-9A-F]{6})>", "");
    }
}
