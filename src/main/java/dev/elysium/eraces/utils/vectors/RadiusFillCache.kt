package dev.elysium.eraces.utils.vectors

object RadiusFillCache {
    private val cache = mutableMapOf<String, List<Vec3>>()

    fun getOrCompute(key: String, generator: () -> List<Vec3>): List<Vec3> {
        return cache.getOrPut(key) { generator() }
    }

    fun clear() = cache.clear()
}