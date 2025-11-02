package dev.elysium.eraces.utils

object TimeParser {
    private val pattern = Regex("""(\d+)([smht])""", RegexOption.IGNORE_CASE)

    private enum class Unit(val symbol: String, val secondsMultiplier: Long, val ticksMultiplier: Long) {
        T("t", 1L / 20, 1L),
        S("s", 1L, 20L),
        M("m", 60L, 1_200L),
        H("h", 3_600L, 72_000L);

        companion object {
            fun fromSymbol(symbol: String): Unit =
                entries.find { it.symbol.equals(symbol, ignoreCase = true) }
                    ?: throw IllegalArgumentException("Неизвестная единица: $symbol")
        }
    }

    private fun parse(input: String): Pair<Long, Unit> {
        val match = pattern.matchEntire(input.trim())
            ?: throw IllegalArgumentException("Неверный формат времени: $input")
        val value = match.groupValues[1].toLong()
        val unit = Unit.fromSymbol(match.groupValues[2])
        return value to unit
    }

    fun parseToSeconds(input: String): Long {
        val (value, unit) = parse(input)
        return when (unit) {
            Unit.T -> value / 20
            else -> value * unit.secondsMultiplier
        }
    }

    fun parseToMilliseconds(input: String): Long =
        parseToSeconds(input) * 1000

    fun parseToTicks(input: String): Long {
        val (value, unit) = parse(input)
        return value * unit.ticksMultiplier
    }
}
