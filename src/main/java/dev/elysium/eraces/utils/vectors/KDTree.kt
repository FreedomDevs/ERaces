package dev.elysium.eraces.utils.vectors


class KDTree(points: List<Vec3>) {
    private val root: KDNode? = if (points.isNotEmpty()) KDNode(points, 0) else null

    private class KDNode(val pts: List<Vec3>, val depth: Int) {
        val axis = depth % 3
        val median: Vec3?
        val left: KDNode?
        val right: KDNode?

        init {
            if (pts.isEmpty()) {
                median = null
                left = null
                right = null
            } else if (pts.size == 1) {
                median = pts[0]
                left = null
                right = null
            } else {
                val sorted = pts.sortedWith(compareBy { p ->
                    when (axis) {
                        0 -> p.x
                        1 -> p.y
                        else -> p.z
                    }
                })
                val mid = sorted.size / 2
                median = sorted[mid]
                left = if (mid > 0) KDNode(sorted.subList(0, mid), depth + 1) else null
                right = if (mid + 1 < sorted.size) KDNode(sorted.subList(mid + 1, sorted.size), depth + 1) else null
            }
        }

        fun queryRadius(center: Vec3, radius: Double, out: MutableList<Vec3>) {
            median?.let { m ->
                if (center.distance(m) <= radius) out.add(m)
                val centerCoord = when (axis) {
                    0 -> center.x
                    1 -> center.y
                    else -> center.z
                }
                val medianCoord = when (axis) {
                    0 -> m.x
                    1 -> m.y
                    else -> m.z
                }
                if (centerCoord - radius <= medianCoord) left?.queryRadius(center, radius, out)
                if (centerCoord + radius >= medianCoord) right?.queryRadius(center, radius, out)
            }
        }
    }

    fun queryRadius(center: Vec3, radius: Double): List<Vec3> {
        val res = mutableListOf<Vec3>()
        root?.queryRadius(center, radius, res)
        return res
    }
}