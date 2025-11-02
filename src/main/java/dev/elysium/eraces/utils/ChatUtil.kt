package dev.elysium.eraces.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.title.Title
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.time.Duration
import java.util.regex.Pattern

object ChatUtil {

    private val mini = MiniMessage.builder()
        .tags(
            TagResolver.builder()
                .resolver(StandardTags.color())
                .resolver(StandardTags.gradient())
                .resolver(StandardTags.rainbow())
                .build()
        ).build()

    private val hexPattern = Pattern.compile("(?i)<#([0-9A-F]{6})>")
    private val legacy = LegacyComponentSerializer.legacySection()

    /**
     * Парсит строку в компонент с поддержкой MiniMessage, legacy-цветов и подстановок.
     *
     * @param text исходный текст.
     * @param args карта замен (например, mapOf("{player}" to player.name)).
     * @return готовый компонент без курсива.
     */
    fun parse(text: String, args: Map<String, String> = emptyMap()): Component {
        val replaced = applyArgs(text, args)
        return when {
            replaced.contains('<') -> clearItalic(mini.deserialize(replaced))
            replaced.contains('&') -> clearItalic(legacy.deserialize(replaced.replace('&', '§')))
            else -> clearItalic(Component.text(replaced))
        }
    }

    /**
     * Перегрузка [parse], принимающая пары аргументов.
     */
    fun parse(text: String, vararg args: Pair<String, String>): Component =
        parse(text, args.toMap())

    /**
     * Создаёт текстовый компонент заданного цвета.
     *
     * @param s текст.
     * @param color цвет [TextColor].
     */
    fun text(s: String, color: TextColor): Component = clearItalic(Component.text(s, color))

    /**
     * Создаёт текстовый компонент с цветом из HEX-строки.
     *
     * @param s текст.
     * @param hex HEX-код цвета (например, "#FFAA00").
     */
    fun text(s: String, hex: String): Component =
        clearItalic(Component.text(s, TextColor.fromHexString(hex)))

    /**
     * Убирает курсив у текста.
     *
     * @param c компонент.
     * @return компонент без курсива.
     */
    fun clearItalic(c: Component): Component =
        c.decoration(TextDecoration.ITALIC, false)

    /**
     * Отправляет сообщение [CommandSender] (игроку или консоли).
     *
     * @param sender получатель.
     * @param component текстовый компонент.
     */
    fun message(sender: CommandSender, component: Component) =
        sender.sendMessage(clearItalic(component))

    /**
     * Отправляет сообщение, предварительно парся текст через [parse].
     *
     * @param sender получатель.
     * @param text текст с возможными MiniMessage/legacy кодами.
     * @param args карта замен.
     */
    fun message(sender: CommandSender, text: String, args: Map<String, String> = emptyMap()) =
        message(sender, parse(text, args))

    /**
     * Упрощённый вариант [message] без аргументов.
     */
    fun message(sender: CommandSender, text: String) =
        message(sender, text, emptyMap())

    /**
     * Отправляет сообщение в action bar игроку.
     *
     * @param player игрок.
     * @param text сообщение.
     * @param args карта замен.
     */
    fun action(player: Player, text: String, args: Map<String, String> = emptyMap()) =
        player.sendActionBar(parse(text, args))

    /**
     * Отправляет заголовок (title + subtitle) игроку.
     *
     * @param player игрок.
     * @param title основной текст.
     * @param subtitle подзаголовок.
     * @param fadeIn длительность появления (мс).
     * @param stay длительность показа (мс).
     * @param fadeOut длительность исчезновения (мс).
     */
    fun title(
        player: Player,
        title: String,
        subtitle: String = "",
        fadeIn: Long = 500,
        stay: Long = 2000,
        fadeOut: Long = 500
    ) = player.showTitle(
        Title.title(
            parse(title),
            parse(subtitle),
            Title.Times.times(
                Duration.ofMillis(fadeIn),
                Duration.ofMillis(stay),
                Duration.ofMillis(fadeOut)
            )
        )
    )

    /** @deprecated Используй [parse] */
    @Deprecated("Use ChatUtil.parse(text, args)")
    fun formatOld(text: String, args: Map<String, String>): Component =
        mini.deserialize(applyArgs(text, args))

    /** @deprecated Используй [parse] */
    @Deprecated("Use ChatUtil.parse(text)")
    fun formatOld(text: String): Component =
        formatOld(text, emptyMap())

    /** @deprecated Используй [parse] */
    @Deprecated("Use ChatUtil.parse(text, args)")
    fun legacyFormat(text: String, args: Map<String, String>): Component {
        val colored = hexToLegacy(applyArgs(text, args)).replace('&', '§')
        return clearItalic(legacy.deserialize(colored))
    }

    /** @deprecated Используй [parse] */
    @Deprecated("Use ChatUtil.parse(text)")
    fun legacyFormat(text: String): Component = legacyFormat(text, emptyMap())

    /** @deprecated Используй [message] */
    @Deprecated("Use ChatUtil.message(sender, text, args)")
    fun messageOld(sender: CommandSender, msg: String, args: Map<String, String>) =
        sender.sendMessage(parse(msg, args))

    /** @deprecated Используй [message] */
    @Deprecated("Use ChatUtil.message(sender, text)")
    fun messageOld(sender: CommandSender, msg: String) =
        messageOld(sender, msg, emptyMap())

    /** @deprecated Используй [message] */
    @Deprecated("Use ChatUtil.message(sender, text, args)")
    fun legacyMessageOld(sender: CommandSender, msg: String, args: Map<String, String>) =
        sender.sendMessage(legacyFormat(msg, args))

    /** @deprecated Используй [message] */
    @Deprecated("Use ChatUtil.message(sender, text)")
    fun legacyMessageOld(sender: CommandSender, msg: String) =
        legacyMessageOld(sender, msg, emptyMap())

    /** @deprecated Используй [title] */
    @Deprecated("Use ChatUtil.title(player, title, subtitle)")
    fun sendTitleOld(player: Player, title: String, subtitle: String, fadeIn: Int, stay: Int, fadeOut: Int) =
        title(player, title, subtitle, fadeIn.toLong(), stay.toLong(), fadeOut.toLong())

    /** @deprecated Используй [title] */
    @Deprecated("Use ChatUtil.title(player, title, subtitle)")
    fun sendTitleOld(player: Player, title: String, subtitle: String) =
        title(player, title, subtitle)

    /** @deprecated Используй [action] */
    @Deprecated("Use ChatUtil.action(player, text)")
    fun sendActionOld(player: Player, msg: String, args: Map<String, String>) =
        player.sendActionBar(parse(msg, args))

    /** @deprecated Используй [action] */
    @Deprecated("Use ChatUtil.action(player, text)")
    fun sendActionOld(player: Player, msg: String) =
        sendActionOld(player, msg, emptyMap())

    private fun applyArgs(text: String, args: Map<String, String>): String {
        var result = text
        args.forEach { (k, v) -> result = result.replace(k, v) }
        return result
    }

    private fun hexToLegacy(text: String): String {
        val matcher = hexPattern.matcher(text)
        val buffer = StringBuffer()
        while (matcher.find()) {
            val hex = matcher.group(1)
            val legacy = buildString {
                append("§x")
                hex.forEach { append('§').append(it) }
            }
            matcher.appendReplacement(buffer, legacy)
        }
        matcher.appendTail(buffer)
        return buffer.toString().replace(Regex("</#([0-9A-F]{6})>"), "")
    }
}

/**
 * Extension-функция для отправки сообщений из команд (CommandSender).
 *
 * @param text сообщение.
 * @param args карта подстановок.
 */
fun CommandSender.msg(text: String, vararg args: Pair<String, String>) =
    ChatUtil.message(this, text, args.toMap())

/**
 * Extension-функция для отправки сообщений в action bar.
 *
 * @param text текст сообщения.
 * @param args карта подстановок.
 */
fun Player.actionMsg(text: String, vararg args: Pair<String, String>) =
    ChatUtil.action(this, text, args.toMap())

/**
 * Extension-функция для отправки заголовков (title/subtitle) игроку.
 *
 * @param title заголовок.
 * @param subtitle подзаголовок (по умолчанию пустой).
 */
fun Player.titleMsg(title: String, subtitle: String = "") =
    ChatUtil.title(this, title, subtitle)