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

public class ChatUtil {
    private static final MiniMessage miniMessage = MiniMessage.builder().tags(TagResolver.builder()
            .resolver(StandardTags.color())
            .resolver(StandardTags.gradient())
            .resolver(StandardTags.rainbow())
            .build()).build();

    public static Component format(String text, Map<String, String> args) {
        return miniMessage.deserialize(applyArgs(text, args));
    }

    public static Component legacyFormat(String text) {
        return clearItalic(LegacyComponentSerializer.legacySection().deserialize(text));
    }

    public static Component text(String s, TextColor clr) {
        return clearItalic(Component.text(s, clr));
    }

    public static Component clearItalic(Component text) {
        return text.decoration(TextDecoration.ITALIC, false);
    }

    public static String applyArgs(String text, Map<String, String> args) {
        String result = text;
        for (Map.Entry<String, String> entry : args.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static void message(CommandSender sender, String msg, Map<String, String> args) {
        sender.sendMessage(format(msg, args));
    }

    public static void message(CommandSender sender, String msg) {
        message(sender, msg, Collections.emptyMap());
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.showTitle(Title.title(
                format(title, Collections.emptyMap()),
                format(subtitle, Collections.emptyMap()),
                Title.Times.times(
                        Duration.ofMillis(fadeIn),
                        Duration.ofMillis(stay),
                        Duration.ofMillis(fadeOut))));
    }

    public static void sendTitle(Player player, String title, String subtitle) {
        sendTitle(player, title, subtitle, 10, 20, 10);
    }

    public static void sendAction(Player player, String msg, Map<String, String> args) {
        Component formatted = format(msg, args);
        player.sendActionBar(formatted);
    }

    public static void sendAction(Player player, String msg) {
        sendAction(player, msg, Collections.emptyMap());
    }
}
