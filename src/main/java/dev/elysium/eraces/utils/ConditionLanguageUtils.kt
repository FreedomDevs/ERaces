package dev.elysium.eraces.utils

import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

object ConditionLanguageUtils {

    /**
     * Проверяет, выполняется ли условие для конкретного игрока.
     */
    fun check(player: Player, condition: String): Boolean =
        condition.isBlank() || condition.equals("ALWAYS", ignoreCase = true) || evaluate(condition, buildContext(player))


    private fun buildContext(player: Player): Map<String, String> {
        val time = player.world.time
        val timeStr = if (time in 0..11999) "DAY" else "NIGHT"

        return mapOf(
            "TIME" to timeStr,
            "WORLD" to player.world.environment.name.uppercase(),
            "BIOME" to player.location.block.biome.key.key.uppercase(),
            "HP" to player.health.toString(),
            "MAX_HP" to player.getAttribute(Attribute.MAX_HEALTH)!!.value.toString(),
            "IS_UNDERWATER" to player.eyeLocation.block.type.name.contains("WATER").toString()
        )
    }

    private fun evaluate(expr: String, ctx: Map<String, String>): Boolean {
        var expression = expr.uppercase().replace("\\s+".toRegex(), " ")
        for ((key, value) in ctx) {
            expression = expression.replace("\\b$key\\b".toRegex(), value)
        }

        return try {
            ExpressionEvaluator().evaluate(expression)
        } catch (e: Exception) {
            println("[ConditionLanguage] Ошибка в выражении: '$expr' -> ${e.message}")
            false
        }
    }

    private class ExpressionEvaluator {
        private val operators = mapOf(
            "&&" to "and",
            "||" to "or",
            ">=" to "≥",
            "<=" to "≤",
            "!=" to "≠",
            "==" to "="
        )

        fun evaluate(expr: String): Boolean {
            val normalized = operators.entries.fold(expr) { acc, (k, v) -> acc.replace(k, v) }.trim()
            return evalBoolean(normalized)
        }

        private fun evalBoolean(expr: String): Boolean {
            split(expr, " or ").forEach { orPart ->
                if (split(orPart, " and ").all { evalComparison(it.trim()) }) return true
            }
            return false
        }

        private fun evalComparison(part: String): Boolean {
            val tokens = part.split("\\s+".toRegex())
            if (tokens.size < 3) return false
            val (left, op, right) = tokens
            return when (op) {
                "=" -> left == right
                "≠" -> left != right
                ">" -> compare(left, right) { l, r -> l > r }
                "<" -> compare(left, right) { l, r -> l < r }
                "≥" -> compare(left, right) { l, r -> l >= r }
                "≤" -> compare(left, right) { l, r -> l <= r }
                else -> false
            }
        }

        private fun compare(a: String, b: String, op: (Double, Double) -> Boolean): Boolean {
            val da = a.toDoubleOrNull() ?: return false
            val db = b.toDoubleOrNull() ?: return false
            return op(da, db)
        }

        private fun split(str: String, delimiter: String): List<String> {
            val result = mutableListOf<String>()
            var balance = 0
            val current = StringBuilder()
            var i = 0
            while (i < str.length) {
                val c = str[i]
                when (c) {
                    '(' -> balance++
                    ')' -> balance--
                }
                if (balance == 0 && str.regionMatches(i, delimiter, 0, delimiter.length)) {
                    result += current.toString()
                    current.clear()
                    i += delimiter.length
                    continue
                }
                current.append(c)
                i++
            }
            result += current.toString()
            return result
        }
    }
}
