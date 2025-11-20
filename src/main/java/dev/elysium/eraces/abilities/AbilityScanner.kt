package dev.elysium.eraces.abilities

import dev.elysium.eraces.abilities.interfaces.IAbility
import io.github.classgraph.ClassGraph
import org.bukkit.plugin.java.JavaPlugin

/**
 * Сканирует указанный пакет и создаёт экземпляры всех способностей,
 * помеченных аннотацией @RegisterAbility.
 */
internal object AbilityScanner {
    fun scan(plugin: JavaPlugin, packageName: String): List<IAbility> {
        val scanResult = ClassGraph()
            .enableAllInfo()
            .acceptPackages(packageName)
            .addClassLoader(plugin.javaClass.classLoader)
            .scan()

        val abilities: MutableList<IAbility> = mutableListOf()

        for (clsInfo in scanResult.getClassesWithAnnotation(RegisterAbility::class.java.name)) {
            try {
                val ability = clsInfo
                    .loadClass()
                    .getDeclaredConstructor()
                    .newInstance() as IAbility

                abilities.add(ability)
            } catch (e: Throwable) {
                plugin.logger.warning(
                    "Не удалось создать способность ${clsInfo.name}: ${e.message}"
                )
            }
        }

        return abilities
    }
}