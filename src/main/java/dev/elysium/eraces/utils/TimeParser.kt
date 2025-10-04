package dev.elysium.eraces.utils

object TimeParser {
    private val pattern = Regex("""(\d+)([smh])""", RegexOption.IGNORE_CASE)

    fun parseToSeconds(input: String): Long {
        val match = pattern.matchEntire(input.trim())
            ?: throw IllegalArgumentException("Неверный формат времени: $input")

        val value = match.groupValues[1].toLong()
        return when (match.groupValues[2].lowercase()) {
            "s" -> value
            "m" -> value * 60
            "h" -> value * 3600
            else -> throw IllegalArgumentException("Неизвестная единица: ${match.groupValues[2]}")
        }
    }

    fun parseToTicks(input: String): Long {
        return parseToSeconds(input) * 20
    }
}