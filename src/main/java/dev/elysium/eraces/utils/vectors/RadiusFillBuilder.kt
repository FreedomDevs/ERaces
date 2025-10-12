package dev.elysium.eraces.utils.vectors

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Универсальный билдер форм/анимаций.
 * Fluent API для создания 3D-сеток точек и анимаций.
 */
class RadiusFillBuilder {

    enum class Shape { SPHERE, CUBE, CIRCLE, STAR }

    sealed class Animation {
        data class Pulse(val amplitude: Double, val periodSec: Double) : Animation()
        data class Wave(val frequency: Double, val amplitude: Double) : Animation()
        data class HorizontalStripes(val stripeCount: Int) : Animation()
    }

    // --- builder state ---
    private var shape: Shape = Shape.SPHERE
    private var radius: Double = 1.0
    private var filled: Boolean = true
    private var step: Double = 0.3
    private var outlineSteps: Int = 36
    private var interpFactor: Int = 1
    private var rotationY: Double = 0.0
    private var animation: Animation? = null
    private var starPoints: Int = 5

    // --- builder API ---

    /**
     * Устанавливает форму сферы с заданным радиусом.
     * @param radius Радиус сферы.
     */
    fun sphere(radius: Double) = apply { shape = Shape.SPHERE; this.radius = radius }

    /**
     * Устанавливает форму куба с указанным размером (edge length).
     * @param size Длина ребра куба.
     */
    fun cube(size: Double) = apply { shape = Shape.CUBE; radius = size }

    /**
     * Устанавливает форму круга с указанным радиусом.
     * @param radius Радиус круга.
     */
    fun circle(radius: Double) = apply { shape = Shape.CIRCLE; this.radius = radius }

    /**
     * Устанавливает форму звезды с радиусом и количеством вершин.
     * @param radius Радиус звезды.
     * @param points Количество вершин (по умолчанию 5).
     */
    fun star(radius: Double, points: Int = 5) = apply { shape = Shape.STAR; this.radius = radius; starPoints = points }

    /**
     * Устанавливает, будет ли форма заполненной или только outline.
     * @param filled true — форма заполнена, false — только outline.
     */
    fun filled(filled: Boolean) = apply { this.filled = filled }

    /**
     * Задает шаг сетки для заполненных форм.
     * @param step Расстояние между точками.
     */
    fun step(step: Double) = apply { this.step = step }

    /**
     * Количество шагов для outline форм (например, сферы или круга).
     * @param steps Количество шагов.
     */
    fun outlineSteps(steps: Int) = apply { outlineSteps = steps.coerceAtLeast(4) }

    /**
     * Интерполяция точек: между каждой парой точек вставляется (factor-1) промежуточных.
     * @param factor Коэффициент интерполяции (1 = без интерполяции).
     */
    fun interpolationFactor(factor: Int) = apply { interpFactor = factor.coerceAtLeast(1) }

    /**
     * Поворот сетки вокруг оси Y на заданный угол.
     * @param deg Угол поворота в градусах.
     */
    fun rotateY(deg: Double) = apply { rotationY = deg }

    /**
     * Анимация пульсации: масштабирует точки формы.
     * @param amplitude Амплитуда пульсации.
     * @param periodSec Период анимации в секундах.
     */
    fun animatePulse(amplitude: Double, periodSec: Double) = apply { animation = Animation.Pulse(amplitude, periodSec) }

    /**
     * Анимация волны: вертикальные смещения точек по синусоиде.
     * @param freq Частота волны.
     * @param amplitude Амплитуда волны.
     */
    fun animateWave(freq: Double, amplitude: Double) = apply { animation = Animation.Wave(freq, amplitude) }

    /**
     * Анимация горизонтальных полос: оставляет точки по полосам.
     * @param stripes Количество горизонтальных полос.
     */
    fun animateHorizontalStripes(stripes: Int) = apply { animation = Animation.HorizontalStripes(stripes) }

    /**
     * Возвращает провайдер точек.
     * Каждый вызов создает копию базовой сетки с применением анимации, поворота и интерполяции.
     * KD-Tree встроен для ускорения поиска точек при анимациях.
     * @return Функция-провайдер списка Vec3.
     */
    fun buildProvider(): () -> List<Vec3> {
        val key = buildKey()
        val base = RadiusFillCache.getOrCompute(key) { generateBase() }

        // строим KD-Tree один раз для ускорения анимаций
        val tree = KDTree(base)

        return {
            val pts = base.map { Vec3(it.x, it.y, it.z) }.toMutableList()
            applyRotation(pts)
            animation?.let { applyAnimationWithKDTree(pts, tree, it) }
            if (interpFactor > 1) densify(pts, interpFactor) else pts
        }
    }

    private fun buildKey(): String =
        "shape=$shape;r=$radius;filled=$filled;step=$step;outline=$outlineSteps;star=$starPoints"

    // --- generate base ---
    private fun generateBase(): List<Vec3> = when (shape) {
        Shape.SPHERE -> generateSphere()
        Shape.CUBE -> generateCube()
        Shape.CIRCLE -> generateCircle()
        Shape.STAR -> generateStar()
    }

    // --- shape generators ---
    private fun generateSphere(): List<Vec3> =
        if (!filled) generateSphereOutline() else generateSphereFilled()

    private fun generateSphereFilled(): List<Vec3> {
        val pts = mutableListOf<Vec3>()
        for (x in (-radius..radius).stepSequence(step))
            for (y in (-radius..radius).stepSequence(step))
                for (z in (-radius..radius).stepSequence(step))
                    if (x * x + y * y + z * z <= radius * radius) pts.add(Vec3(x, y, z))
        return pts
    }

    private fun generateSphereOutline(): List<Vec3> = fibonacciSpherePoints(radius, outlineSteps * 4.coerceAtLeast(100))

    private fun generateCube(): List<Vec3> =
        if (filled) generateCubeFilled() else generateCubeOutline()

    private fun generateCubeFilled(): List<Vec3> {
        val pts = mutableListOf<Vec3>()
        for (x in (-radius..radius).stepSequence(step))
            for (y in (-radius..radius).stepSequence(step))
                for (z in (-radius..radius).stepSequence(step))
                    pts.add(Vec3(x, y, z))
        return pts
    }

    private fun generateCubeOutline(): List<Vec3> {
        val pts = mutableListOf<Vec3>()
        for (x in (-radius..radius).stepSequence(step))
            for (y in (-radius..radius).stepSequence(step)) {
                pts.add(Vec3(x, y, -radius))
                pts.add(Vec3(x, y, radius))
            }
        for (z in (-radius..radius).stepSequence(step))
            for (y in (-radius..radius).stepSequence(step)) {
                pts.add(Vec3(-radius, y, z))
                pts.add(Vec3(radius, y, z))
            }
        return pts
    }

    private fun generateCircle(): List<Vec3> {
        val pts = mutableListOf<Vec3>()
        val n = outlineSteps.coerceAtLeast(6)
        val delta = 2.0 * PI / n
        if (!filled) {
            var x = radius
            var z = 0.0
            val cosd = cos(delta)
            val sind = sin(delta)
            repeat(n) {
                pts.add(Vec3(x, 0.0, z))
                val nx = x * cosd - z * sind
                val nz = x * sind + z * cosd
                x = nx; z = nz
            }
        } else {
            val rings = n
            val stepDist = radius / rings
            for (ri in 1..rings) {
                val r = ri * stepDist
                var x = r
                var z = 0.0
                val cosd = cos(delta)
                val sind = sin(delta)
                repeat(n) {
                    pts.add(Vec3(x, 0.0, z))
                    val nx = x * cosd - z * sind
                    val nz = x * sind + z * cosd
                    x = nx; z = nz
                }
            }
        }
        return pts
    }

    private fun generateStar(): List<Vec3> {
        val pts = mutableListOf<Vec3>()
        val n = starPoints.coerceAtLeast(5)
        val stepAngle = 2.0 * PI / n
        var angle = 0.0
        repeat(n) {
            val x = radius * cos(angle)
            val z = radius * sin(angle)
            pts.add(Vec3(x, 0.0, z))
            if (filled) {
                val ix = radius * 0.5 * cos(angle + stepAngle / 2)
                val iz = radius * 0.5 * sin(angle + stepAngle / 2)
                pts.add(Vec3(ix, 0.0, iz))
            }
            angle += stepAngle
        }
        return pts
    }

    // --- interpolation ---
    private fun densify(src: List<Vec3>, factor: Int): MutableList<Vec3> {
        val out = mutableListOf<Vec3>()
        for (i in src.indices) {
            val a = src[i]
            val b = src[(i + 1) % src.size]
            out.add(Vec3(a.x, a.y, a.z))
            for (k in 1 until factor) {
                val t = k.toDouble() / factor
                out.add(Vec3(a.x + (b.x - a.x) * t, a.y + (b.y - a.y) * t, a.z + (b.z - a.z) * t))
            }
        }
        return out
    }

    private fun applyAnimationWithKDTree(points: MutableList<Vec3>, tree: KDTree, animation: Animation) {
        val t = System.currentTimeMillis() / 1000.0
        when (animation) {
            is Animation.Pulse -> {
                val scale = 1.0 + animation.amplitude * sin(t * 2 * PI / animation.periodSec)
                points.forEach { p -> p.x *= scale; p.y *= scale; p.z *= scale }
            }
            is Animation.Wave -> {
                points.forEach { p ->
                    val neighbors = tree.queryRadius(p, 1.0) // локальная область для волны
                    neighbors.forEach { n -> n.y += animation.amplitude * sin(n.x * animation.frequency + t) }
                }
            }
            is Animation.HorizontalStripes -> {
                if (points.isEmpty()) return
                val minY = points.minOf { it.y }
                val maxY = points.maxOf { it.y }
                val stripeH = (maxY - minY) / animation.stripeCount
                val phase = t % animation.stripeCount
                points.removeIf { p ->
                    val neighbors = tree.queryRadius(p, 0.1)
                    neighbors.all { n -> (((n.y - minY) / stripeH + phase) % 1.0) > 0.5 }
                }
            }
        }
    }

    private fun applyRotation(points: MutableList<Vec3>) {
        if (rotationY != 0.0) points.forEach { Vec3Utils.rotateY(it, rotationY) }
    }

    // --- fibonacci sphere ---
    private fun fibonacciSpherePoints(radius: Double, count: Int): List<Vec3> {
        val pts = mutableListOf<Vec3>()
        val phi = PI * (3.0 - sqrt(5.0))
        repeat(count) { i ->
            val y = 1.0 - (i.toDouble() / (count - 1)) * 2.0
            val r = sqrt(1.0 - y * y)
            val theta = phi * i
            pts.add(Vec3(cos(theta) * r * radius, y * radius, sin(theta) * r * radius))
        }
        return pts
    }
}

// --- helper extension ---
private fun ClosedFloatingPointRange<Double>.stepSequence(step: Double) = sequence {
    var current = start
    while (current <= endInclusive) {
        yield(current)
        current += step
    }
}
