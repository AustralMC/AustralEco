package net.australmc.economy.command.money

import net.australmc.economy.command.AbstractCommand
import net.australmc.economy.command.SubcommandExecutor
import net.australmc.economy.extension.sendMessage
import net.australmc.economy.locale.LocaleProvider.getPrefix
import net.australmc.economy.locale.MessagePrefix.NORMAL
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand

class HelpSubcommand : SubcommandExecutor() {
    override val name: String = "help"
    override val alias: List<String> = listOf("ajuda", "comandos", "cmds", "h")
    override val correctUsage: String = "/money help"
    override val description: String = "Ver informações a respeito do comando."
    override val playerOnly: Boolean = false

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {
        val moneyCommand = (cmd as? PluginCommand)?.executor as? MoneyCommand
        val commands: Set<AbstractCommand?> = setOf(*MoneyCommand.subCommands.toTypedArray(), moneyCommand)

        val message = mutableListOf(
            "${getPrefix(NORMAL)} §6- §eInformação do comando §a/money",
        )

        commands.forEach { if(it != null) message.add("§6${it.correctUsage} §a- §e${it.description}") }

        sender.sendMessage(message)

        return true
    }
}