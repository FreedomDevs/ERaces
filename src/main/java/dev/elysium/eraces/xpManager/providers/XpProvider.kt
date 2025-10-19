package dev.elysium.eraces.xpManager.providers

interface XpProvider<T> {
    fun getXp(target: T): Long?
}