package net.australmc.economy.command.admin

import net.australmc.economy.domain.Role.ADMIN
import net.australmc.economy.domain.Role.MODERATOR
import net.australmc.economy.exception.catchCommandEconomyException
import net.australmc.economy.locale.LocaleProvider.getMappedMessage
import net.australmc.economy.locale.Message.ADMIN_TAKE_SUCCESS
import net.australmc.economy.service.money.takeFromPlayerBalanceService
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class TakeSubcommand : AdminSubcommandExecutor() {
    override val name = "take"
    override val alias = listOf("remover", "remove", "withdraw", "tirar")
    override val playerOnly = false
    override val correctUsage = "/eco take <jogador> <quantia>"
    override val description = "Remover uma quantia de dinheiro de um jogador."
    override val allowedRoles = setOf(ADMIN, MODERATOR)

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>) : Boolean {
        if(!validateAdminEconomyOperationSubcommand(sender, args)) return true

        val target = Bukkit.getPlayer(args[0])!!
        val amount = args[1].toDouble()

        catchCommandEconomyException(sender,
            { takeFromPlayerBalanceService(target, amount) },
            { sender.sendMessage(
                getMappedMessage(
                    ADMIN_TAKE_SUCCESS, mapOf(
                        "jogador" to target.name,
                        "valor" to amount.toString()
                    )
                )
            ) }
        )

        return true
    }

}