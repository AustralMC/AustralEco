package net.australmc.economy.utils

import net.australmc.economy.event.listener.DataManagementEventListener
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

private val LISTENERS = setOf<Class<out Listener>>(DataManagementEventListener::class.java)

fun registerAllListeners(plugin: JavaPlugin) {
    LISTENERS.forEach { listenerClass -> Bukkit.getPluginManager()
        .registerEvents(listenerClass.getDeclaredConstructor().newInstance(), plugin) }
}