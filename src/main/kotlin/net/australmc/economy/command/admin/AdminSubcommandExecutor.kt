package net.australmc.economy.command.admin

import net.australmc.economy.command.SubcommandExecutor
import net.australmc.economy.locale.LocaleProvider
import net.australmc.economy.locale.Message
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

abstract class AdminSubcommandExecutor : SubcommandExecutor(), AdminCommand {
    fun validateAdminEconomyOperationSubcommand(sender: CommandSender, args: Array<String>) : Boolean {
        if(args.size != 2) {
            sender.sendMessage(LocaleProvider.getCorrectUsageMessage(correctUsage))
            return false
        }

        if(Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(LocaleProvider[Message.PLAYER_NOT_FOUND])
            return false
        }

        if(args[1].toDoubleOrNull() == null) {
            sender.sendMessage(
                LocaleProvider.getMappedMessage(
                    Message.PARAMETER_MUST_BE_NUMBER,
                    mapOf("parametro" to "quantia")
                )
            )
            return false
        }

        return true
    }
}
