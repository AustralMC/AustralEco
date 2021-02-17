package net.australmc.economy.command

import net.australmc.economy.locale.LocaleProvider
import net.australmc.economy.locale.Message
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

abstract class SubcommandExecutor : AbstractCommand {

    abstract fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>) : Boolean

    fun execute(sender: CommandSender, cmd: Command, label: String, args: Array<String>) : Boolean {
        if(playerOnly && sender !is Player) {
            sender.sendMessage(LocaleProvider[Message.ONLY_INGAME_COMMAND])
            return true
        }

        return onCommand(sender, cmd, label, args)
    }

    companion object {
        fun isSubcommand(firstArg: String, subcommands: Set<SubcommandExecutor>): Boolean =
            (getSubcommand(firstArg, subcommands) != null)

        fun getSubcommand(firstArg: String, subcommands: Set<SubcommandExecutor>): SubcommandExecutor? =
            subcommands.find { subcommand -> subcommand.name == firstArg || subcommand.alias.contains(firstArg) }
    }

}