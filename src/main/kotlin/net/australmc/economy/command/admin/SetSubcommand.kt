package net.australmc.economy.command.admin

import net.australmc.economy.domain.Role.ADMIN
import net.australmc.economy.exception.catchCommandEconomyException
import net.australmc.economy.locale.LocaleProvider
import net.australmc.economy.locale.LocaleProvider.getMappedMessage
import net.australmc.economy.locale.Message.ADMIN_SET_SUCCESS
import net.australmc.economy.locale.Message.INVALID_AMOUNT
import net.australmc.economy.service.money.setPlayerBalanceService
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class SetSubcommand : AdminSubcommandExecutor() {
    override val name = "set"
    override val alias = listOf("definir")
    override val playerOnly = false
    override val correctUsage = "/eco set <jogador> <quantia>"
    override val description = "Definir uma nova quantia de dinheiro a um jogador, descartando a anterior."
    override val allowedRoles = setOf(ADMIN)

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>) : Boolean {
        if(!validateAdminEconomyOperationSubcommand(sender, args) || !validate(sender, args[1].toDouble()))
            return true

        val target = Bukkit.getPlayer(args[0])!!
        val amount = args[1].toDouble()

        catchCommandEconomyException(sender,
            { setPlayerBalanceService(target, amount) },
            { sender.sendMessage(
                getMappedMessage(
                    ADMIN_SET_SUCCESS, mapOf(
                        "jogador" to target.name,
                        "valor" to amount.toString()
                    )
                )
            ) }
        )

        return true
    }

    private fun validate(sender: CommandSender, amount: Double) : Boolean {
        if(amount < 0) {
            sender.sendMessage(LocaleProvider[INVALID_AMOUNT])
            return false
        }

        return true
    }
}