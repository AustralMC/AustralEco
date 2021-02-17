package net.australmc.economy.utils

import net.australmc.core.commands.CommandRegistrator
import net.australmc.economy.command.admin.AustralEcoCommand
import net.australmc.economy.command.money.MoneyCommand
import org.bukkit.command.CommandExecutor
import org.bukkit.plugin.java.JavaPlugin


fun registerAllCommands(plugin: JavaPlugin) {
    val commandsSet: Set<Class<out CommandExecutor>> = mutableSetOf(
        AustralEcoCommand::class.java,
        MoneyCommand::class.java)

    CommandRegistrator.registerAllCommands(plugin, commandsSet)
}