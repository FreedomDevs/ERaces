package dev.elysium.eraces.utils

import dev.elysium.eraces.ERaces
import org.apache.logging.log4j.util.PropertySource
import java.util.Locale
import java.util.Scanner
import kotlin.math.roundToLong

class TimeValue(val milliseconds: Long, val input: String = "0s") {
    /**Парсит длительность из строки типа "2d 1m 1s"
     * Может принимать дроби "1.2d"
     * Парсит так:
     * - Пропускает всё пока не найдёт число
     * - После числа берёт первую букву, умножает число на множитель и прибавляет к сумме (если буквы не нашлось, число которое было просто забывается)
     * - Снова пропускает всё пока не найдёт число
     *
     * Таким образом строка "Hello World! Арбуз 2dхуй5tхуй" остаётся валидной, но будет выдавать предупреждения в консоль
     *
     * Еденицы измерения:
     * - y — годы
     * - w — недели
     * - d — дни
     * - h - часы
     * - m — минуты
     * - s — секунды
     * - t — тики (в секунде 20 тиков)
     *
     * @param input Вводная строка
     * @throws IllegalArgumentException Если не удалось спарсить вообще ничего
     */
    constructor(input: String) : this(parseDuration(input), input)

    fun toTicks(): Long = milliseconds / 50
    fun toTicksInt(): Int {
        val ticks = milliseconds / 50
        if (ticks > Int.MAX_VALUE) {
            ERaces.logger().warning(
                "Внимание! Заданное время (${milliseconds}мс) слишком велико для Int. " +
                        "Число тиков ($ticks) превышает лимит Int.MAX_VALUE (${Int.MAX_VALUE}). " +
                        "Значение автоматически урезано до максимума."
            )
            return Int.MAX_VALUE
        }

        return ticks.toInt()
    }

    fun toSeconds(): Double = milliseconds.toDouble() / 1000

    fun prettify(
        withYears: Boolean = true,
        withWeeks: Boolean = true,
        withDays: Boolean = true,
        withHours: Boolean = true,
        withMinutes: Boolean = true,
        withSeconds: Boolean = true,
        withTicks: Boolean = false,
        withMilliseconds: Boolean = false
    ): String {
        if (milliseconds == 0L) return "0 секунд"
        var remaining: Long = milliseconds
        val result = StringBuilder()
        val units = listOf(
            UnitPart(withYears, MS_PER_YEAR, "год", "года", "лет"),
            UnitPart(withWeeks, MS_PER_WEEK, "неделя", "недели", "недель"),
            UnitPart(withDays, MS_PER_DAY, "день", "дня", "дней"),
            UnitPart(withHours, MS_PER_HOUR, "час", "часа", "часов"),
            UnitPart(withMinutes, MS_PER_MINUTE, "минута", "минуты", "минут"),
            UnitPart(withSeconds, MS_PER_SECOND, "секунда", "секунды", "секунд"),
            UnitPart(withTicks, MS_PER_TICK, "тик", "тика", "тиков"),
            UnitPart(withMilliseconds, 1, "миллисекунда", "миллисекунды", "миллисекунд"),
        )

        for (u in units) {
            if (!u.enabled) continue

            val part = genPart(remaining, u)
            remaining = part.first
            result.append(part.second)
        }

        return result.toString()
    }

    companion object {
        const val MS_PER_TICK = 50L
        const val MS_PER_SECOND = 1000L
        const val MS_PER_MINUTE = 60 * 1000L
        const val MS_PER_HOUR = 60 * 60 * 1000L
        const val MS_PER_DAY = 24 * 60 * 60 * 1000L
        const val MS_PER_WEEK = 7 * 24 * 60 * 60 * 1000L
        const val MS_PER_YEAR = (365.2425 * 24 * 60 * 60 * 1000).toLong()

        fun fromSeconds(seconds: Double) = TimeValue((seconds * MS_PER_SECOND).toLong(), seconds.toString() + "s")
        fun fromSeconds(seconds: Long) = TimeValue(seconds * MS_PER_SECOND, seconds.toString() + "s")
        fun fromSeconds(seconds: Int) = TimeValue(seconds * MS_PER_SECOND, seconds.toString() + "s")

        fun parseDuration(input: String): Long {
            val cleanInput = input.lowercase().replace(',', '.')

            val scanner = Scanner(cleanInput).useLocale(Locale.US)

            var totalMillis = 0L
            var isValid = false

            while (scanner.hasNext()) {
                if (scanner.hasNextDouble()) {
                    val value = scanner.nextDouble()

                    val unitStr = scanner.findInLine("[ywdhmst]")

                    if (unitStr == null) {
                        ERaces.logger()
                            .warning("Внимание: Пропущена или неверно указана единица измерения после числа $value в $cleanInput")
                        continue
                    }

                    totalMillis += convertToMillis(value, unitStr)
                    isValid = true
                } else {
                    val char = scanner.nextByte().toInt().toChar().toString()
                    if (char != " ") {
                        ERaces.logger()
                            .warning("Внимание: Обнаружен и проигнорирован невалидный символ: '$char' в $cleanInput")
                    }
                }
            }

            if (!isValid)
                throw IllegalArgumentException("Невалидное время")

            return totalMillis
        }

        fun convertToMillis(value: Double, char: String): Long {
            return when (char) {
                "y" -> (value * MS_PER_YEAR).roundToLong()
                "w" -> (value * MS_PER_WEEK).roundToLong()
                "d" -> (value * MS_PER_DAY).roundToLong()
                "h" -> (value * MS_PER_HOUR).roundToLong()
                "m" -> (value * MS_PER_MINUTE).roundToLong()
                "s" -> (value * MS_PER_SECOND).roundToLong()
                "t" -> (value * MS_PER_TICK).roundToLong()
                else -> throw RuntimeException("Невозможное состояние convertToMillis")
            }
        }

        private fun pluralize(count: Long, one: String, two: String, five: String): String {
            val absCount = kotlin.math.abs(count)
            val mod10 = absCount % 10
            val mod100 = absCount % 100

            if (count == 0L) {
                return ""
            }

            return when {
                mod100 in 11..14 -> " $count $five"
                mod10 == 1L -> " $count $one"
                mod10 in 2..4 -> " $count $two"
                else -> " $count $five"
            }
        }

        private fun genPart(remaining: Long, unitPart: UnitPart): Pair<Long, String> {
            return Pair(
                remaining % unitPart.ms,
                pluralize(remaining / unitPart.ms, unitPart.one, unitPart.two, unitPart.five)
            )
        }

        private data class UnitPart(
            val enabled: Boolean,
            val ms: Long,
            val one: String,
            val two: String,
            val five: String
        )
    }
}