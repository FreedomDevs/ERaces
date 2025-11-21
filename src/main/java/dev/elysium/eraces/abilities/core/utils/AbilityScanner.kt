package dev.elysium.eraces.abilities.core.utils

import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.core.activator.AbilityFactory
import dev.elysium.eraces.abilities.interfaces.IAbility
import io.github.classgraph.ClassGraph
import org.bukkit.plugin.java.JavaPlugin
import kotlin.reflect.KClass

/**
 * Сканирует указанный пакет и создаёт экземпляры всех способностей,
 * помеченных аннотацией @RegisterAbility.
 */
object AbilityScanner {
    fun scan(plugin: JavaPlugin, packageName: String, factory: AbilityFactory): List<IAbility> {
        val scanResult = ClassGraph()
            .enableAllInfo()
            .acceptPackages(packageName)
            .addClassLoader(plugin.javaClass.classLoader)
            .scan()

        val abilities: MutableList<IAbility> = mutableListOf()

        for (clsInfo in scanResult.getClassesWithAnnotation(RegisterAbility::class.java.name)) {
            try {
                val abilityClass = clsInfo.loadClass().kotlin
                val ability = factory.create(abilityClass as KClass<IAbility>)
                abilities.add(ability)
            } catch (e: Throwable) {
                plugin.logger.log(
                    java.util.logging.Level.SEVERE,
                    "Ошибка при создании способности ${clsInfo.name}",
                    e
                )
            }

        }

        return abilities
    }
}