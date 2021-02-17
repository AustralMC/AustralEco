package net.australmc.economy.command.admin

import net.australmc.economy.command.AbstractCommand
import net.australmc.economy.domain.Role

interface AdminCommand : AbstractCommand {
    val allowedRoles: Set<Role>
}