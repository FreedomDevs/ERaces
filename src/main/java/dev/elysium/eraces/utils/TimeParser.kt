package dev.elysium.eraces.utils

object TimeParser {
    private val pattern = Regex("""(\d+(?:\.\d+)?)([smht])""", RegexOption.IGNORE_CASE)

    private enum class Unit(val symbol: String, val secondsMultiplier: Double, val ticksMultiplier: Double) {
        T("t", 1.0 / 20.0, 1.0),
        S("s", 1.0, 20.0),
        M("m", 60.0, 1_200.0),
        H("h", 3_600.0, 72_000.0);

        companion object {
            fun fromSymbol(symbol: String): Unit =
                entries.find { it.symbol.equals(symbol, ignoreCase = true) }
                    ?: throw IllegalArgumentException("Неизвестная единица: $symbol")
        }
    }

    private fun parse(input: String): Pair<Double, Unit> {
        val match = pattern.matchEntire(input.trim())
            ?: throw IllegalArgumentException("Неверный формат времени: $input")
        val value = match.groupValues[1].toDouble()
        val unit = Unit.fromSymbol(match.groupValues[2])
        return value to unit
    }

    fun parseToSeconds(input: String): Double {
        val (value, unit) = parse(input)
        return value * unit.secondsMultiplier
    }

    fun parseToMilliseconds(input: String): Long =
        (parseToSeconds(input) * 1000).toLong()

    fun parseToTicks(input: String): Long {
        val (value, unit) = parse(input)
        return (value * unit.ticksMultiplier).toLong()
    }
}
