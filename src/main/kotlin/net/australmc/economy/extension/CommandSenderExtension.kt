package net.australmc.economy.extension

import net.australmc.economy.domain.Role
import org.bukkit.command.CommandSender

fun CommandSender.hasRole(role: Role) = this.hasPermission(role.permissionNode)
fun CommandSender.hasAnyRole(roles: Set<Role>) = roles.stream().anyMatch { this.hasPermission(it.permissionNode) }
fun CommandSender.sendMessage(message: List<String>) = message.forEach { this.sendMessage(it) }