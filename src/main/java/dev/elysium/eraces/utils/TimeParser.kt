package dev.elysium.eraces.utils

import dev.elysium.eraces.ERaces
import java.util.Locale
import java.util.Scanner
import kotlin.math.roundToLong

class TimeParser(private val milliseconds: Long) {
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
    constructor(input: String) : this(parseDuration(input))

    fun toMilliseconds(): Long = milliseconds

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
        withYears: Boolean = false,
        withWeeks: Boolean = false,
        withDays: Boolean = false,
        withHours: Boolean = false,
        withMinutes: Boolean = false,
        withSeconds: Boolean = false,
        withTicks: Boolean = false,
        withMilliseconds: Boolean = false
    ): String {
        if (milliseconds == 0L) return "0 секунд"

        var remaining: Long = milliseconds
        var result = ""

        if (withYears) {
            val years = genPart(remaining, (365.2425 * 24 * 60 * 60 * 1000).toLong(), "год", "года", "лет")
            remaining = years.first
            result += years.second
        }

        if (withWeeks) {
            val weeks = genPart(remaining, 7 * 24 * 60 * 60 * 1000, "неделя", "недели", "недель")
            remaining = weeks.first
            result += weeks.second
        }

        if (withDays) {
            val days = genPart(remaining, 24 * 60 * 60 * 1000, "день", "дня", "дней")
            remaining = days.first
            result += days.second
        }

        if (withHours) {
            val hours = genPart(remaining, 60 * 60 * 1000, "час", "часа", "часов")
            remaining = hours.first
            result += hours.second
        }

        if (withMinutes) {
            val minutes = genPart(remaining, 60 * 1000, "минута", "минуты", "минут")
            remaining = minutes.first
            result += minutes.second
        }

        if (withSeconds) {
            val seconds = genPart(remaining, 1000, "секунда", "секунды", "секунд")
            remaining = seconds.first
            result += seconds.second
        }

        if (withTicks) {
            val ticks = genPart(remaining, 50, "тик", "тика", "тиков")
            remaining = ticks.first
            result += ticks.second
        }

        if (withMilliseconds) {
            val milliseconds = genPart(remaining, 1, "миллисекунда", "миллисекунды", "миллисекунд")
            remaining = milliseconds.first
            result += milliseconds.second
        }

        return result
    }

    companion object {
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
                "y" -> (value * (365.2425 * 24 * 60 * 60 * 1000)).roundToLong()
                "w" -> (value * (7 * 24 * 60 * 60 * 1000)).roundToLong()
                "d" -> (value * (24 * 60 * 60 * 1000)).roundToLong()
                "h" -> (value * (60 * 60 * 1000)).roundToLong()
                "m" -> (value * (60 * 1000)).roundToLong()
                "s" -> (value * 1000).roundToLong()
                "t" -> (value * 50).roundToLong()
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

        private fun genPart(remaining: Long, msPer: Long, one: String, two: String, five: String): Pair<Long, String> {
            return Pair(remaining % msPer, pluralize(remaining / msPer, one, two, five))
        }
    }
}