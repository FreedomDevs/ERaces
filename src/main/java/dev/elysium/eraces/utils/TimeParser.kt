package dev.elysium.eraces.utils

object TimeParser {
    private val pattern = Regex("""(\d+)([smht])""", RegexOption.IGNORE_CASE)

    /**
     * Преобразует строку в количество секунд (целое число).
     *
     * @param input строка формата `"<число><единица>"`, например `"10s"`, `"5m"`, `"100t"`.
     * @return количество секунд (округлённое до целого).
     * @throws IllegalArgumentException если формат строки неверный или неизвестная единица измерения.
     * @deprecated Используй [parseToSecondsDouble] для более точного результата.
     */
    @Deprecated(
        message = "Данный метод является менее точным чем parseToSecondsDouble.",
        replaceWith = ReplaceWith("parseToSecondsDouble(input)"),
        level = DeprecationLevel.WARNING
    )
    fun parseToSeconds(input: String): Long {
        val (value, unit) = parse(input)
        return when (unit) {
            Unit.T -> (value / 20).toLong()
            else -> (value * unit.secondsMultiplier).toLong()
        }
    }

    /**
     * Преобразует строку в количество миллисекунд (целое число).
     *
     * @param input строка формата `"<число><единица>"`.
     * @return количество миллисекунд (округлённое до целого).
     * @throws IllegalArgumentException если формат строки неверный.
     * @deprecated Используй [parseToMillisecondsDouble] для большей точности.
     */
    @Deprecated(
        message = "Данный метод является менее точным чем parseToMillisecondsDouble.",
        replaceWith = ReplaceWith("parseToMillisecondsDouble(input)"),
        level = DeprecationLevel.WARNING
    )
    fun parseToMilliseconds(input: String): Long =
        parseToSeconds(input) * 1000

    /**
     * Преобразует строку в количество тиков (целое число).
     *
     * @param input строка формата `"<число><единица>"`.
     * @return количество тиков (округлённое до целого).
     * @throws IllegalArgumentException если формат строки неверный.
     * @deprecated Используй [parseToTicksDouble] для большей точности.
     */
    @Deprecated(
        message = "Данный метод является менее точным чем parseToTicksDouble.",
        replaceWith = ReplaceWith("parseToTicksDouble(input)"),
        level = DeprecationLevel.WARNING
    )
    fun parseToTicks(input: String): Long {
        val (value, unit) = parse(input)
        return (value * unit.ticksMultiplier).toLong()
    }

    /**
     * Преобразует строку в количество секунд (с плавающей точкой).
     *
     * @param input строка формата `"<число><единица>"`, например `"1.5m"`, `"10s"`.
     * @return количество секунд в виде [Double].
     * @throws IllegalArgumentException если формат строки неверный или неизвестная единица измерения.
     */
    fun parseToSecondsDouble(input: String): Double {
        val (value, unit) = parse(input)
        return when (unit) {
            Unit.T -> value / 20.0
            else -> value * unit.secondsMultiplier
        }
    }

    /**
     * Преобразует строку в количество миллисекунд (с плавающей точкой).
     *
     * @param input строка формата `"<число><единица>"`.
     * @return количество миллисекунд в виде [Double].
     * @throws IllegalArgumentException если формат строки неверный.
     */
    fun parseToMillisecondsDouble(input: String): Double =
        parseToSecondsDouble(input) * 1000.0

    /**
     * Преобразует строку в количество тиков (с плавающей точкой).
     *
     * @param input строка формата `"<число><единица>"`.
     * @return количество тиков в виде [Double].
     * @throws IllegalArgumentException если формат строки неверный.
     */
    fun parseToTicksDouble(input: String): Double {
        val (value, unit) = parse(input)
        return value * unit.ticksMultiplier
    }

//    Helpers
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
}
