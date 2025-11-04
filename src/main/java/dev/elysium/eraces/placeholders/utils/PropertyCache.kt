package dev.elysium.eraces.placeholders.utils

import kotlin.reflect.KProperty1

object PropertyCache {
    private val cache = mutableMapOf<String, List<KProperty1<Any, *>>>()

    fun getOrPut(key: String, builder: () -> List<KProperty1<Any, *>>): List<KProperty1<Any, *>> {
        return cache.getOrPut(key.lowercase(), builder)
    }
}