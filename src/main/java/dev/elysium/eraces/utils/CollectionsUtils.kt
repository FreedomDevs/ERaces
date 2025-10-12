package dev.elysium.eraces.utils

object CollectionsUtils {
    fun <T> uniqueBy(list: List<T>, key: (T) -> Any): List<T> {
        val seen = mutableSetOf<Any>()
        val result = mutableListOf<T>()
        for (item in list) {
            val k = key(item)
            if (seen.add(k)) result.add(item)
        }
        return result
    }

    fun <T> insertionSort(list: MutableList<T>, comparator: (T, T) -> Boolean) {
        for (i in 1 until list.size) {
            val current = list[i]
            var j = i - 1
            while (j >= 0 && comparator(list[j], current)) {
                list[j + 1] = list[j]
                j--
            }
            list[j + 1] = current
        }
    }

    class TargetCollector<T> {
        private val items = mutableListOf<Pair<T, Double>>()
        private val collected = mutableSetOf<T>()

        fun add(item: T, distance: Double) {
            if (item !in collected) {
                items.add(item to distance)
                collected.add(item)
            }
        }

        fun contains(item: T): Boolean = item in collected

        fun getSortedByDistance(descending: Boolean = true): List<T> {
            val sorted = items.toMutableList()
            insertionSort(sorted) { a, b -> if (descending) a.second > b.second else a.second < b.second }
            return sorted.map { it.first }
        }
    }
}
