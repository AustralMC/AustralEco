package net.australmc.economy.command.admin

import net.australmc.core.annotations.command.CommandClass
import net.australmc.economy.command.SubcommandExecutor.Companion.getSubcommand
import net.australmc.economy.command.SubcommandExecutor.Companion.isSubcommand
import net.australmc.economy.domain.Role.ADMIN
import net.australmc.economy.domain.Role.MODERATOR
import net.australmc.economy.extension.hasAnyRole
import net.australmc.economy.extension.hasRole
import net.australmc.economy.extension.sendMessage
import net.australmc.economy.locale.LocaleProvider
import net.australmc.economy.locale.LocaleProvider.getPrefix
import net.australmc.economy.locale.Message.NO_PERMISSION
import net.australmc.economy.locale.MessagePrefix.NORMAL
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

@CommandClass(name = "australeco")
class AustralEcoCommand : CommandExecutor, AdminCommand {

    override val name: String = "australeco"
    override val alias: List<String> = listOf("eco")
    override val playerOnly: Boolean = false
    override val correctUsage: String = "/eco"
    override val description: String = "Comando base; Quando não recebe argumentos, envia esse guia."
    override val allowedRoles = setOf(ADMIN, MODERATOR)

    companion object {
        private val subCommands = setOf(GiveSubcommand(), SetSubcommand(), TakeSubcommand())
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) : Boolean {
        if(allowedRoles.stream().noneMatch { sender.hasRole(it) }) {
            sender.sendMessage(LocaleProvider[NO_PERMISSION])
            return true
        }

        if(args.isNotEmpty()) {
            val firstArg = args[0]

            if(isSubcommand(firstArg, subCommands)) {
                val subCommandArgs = args.drop(1).toTypedArray()
                val subCommand = getSubcommand(firstArg, subCommands)!! as AdminSubcommandExecutor

                if(!(sender.hasAnyRole(subCommand.allowedRoles))) {
                    sender.sendMessage(LocaleProvider[NO_PERMISSION])
                    return true
                }

                return subCommand
                    .execute(sender, command, label, subCommandArgs)
            }
        }

        sender.sendMessage(getHelpMessage())
        return true
    }

    private fun getHelpMessage() : List<String> {
        val commands = setOf(this, *subCommands.toTypedArray())

        val resultMessage = mutableListOf(
            "${getPrefix(NORMAL)} §6- §eComandos administrativos",
        )

        commands.forEach {
            resultMessage.add("§6${it.correctUsage} §a- §e${it.description}")
        }

        return resultMessage
    }

}