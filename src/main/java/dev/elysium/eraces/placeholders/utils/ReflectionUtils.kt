package dev.elysium.eraces.placeholders.utils

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

object ReflectionUtils {

    @Suppress("UNCHECKED_CAST")
    fun buildPropertyChain(
        startClass: KClass<*>,
        parts: List<String>,
        aliases: Map<String, String> = emptyMap()
    ): List<KProperty1<Any, *>>? {
        var currentClass = startClass
        val chain = mutableListOf<KProperty1<Any, *>>()

        for (partRaw in parts) {
            val (part, index) = parseIndexedPart(partRaw)
            val propName = aliases[part.lowercase()] ?: part
            val prop = currentClass.memberProperties
                .find { it.name.equals(propName, ignoreCase = true) } as? KProperty1<Any, *>
                ?: return null

            chain += prop

            val returnType = prop.returnType.classifier as? KClass<*> ?: return null
            currentClass = returnType
        }

        return chain
    }

    fun parseIndexedPart(raw: String): Pair<String, Int?> {
        val regex = Regex("(\\w+)_?(\\d+)?")
        val match = regex.matchEntire(raw) ?: return raw to null
        val name = match.groupValues[1]
        val index = match.groupValues.getOrNull(2)?.toIntOrNull()
        return name to index
    }

    fun formatValue(value: Any): String = when (value) {
        is Enum<*> -> value.name.lowercase()
        is Number, is Boolean, is String -> value.toString()
        is Collection<*> -> value.joinToString(", ") { it?.toString() ?: "null" }
        is Map<*, *> -> value.entries.joinToString(", ") { "${it.key}=${it.value}" }
        else -> value.toString()
    }
}
