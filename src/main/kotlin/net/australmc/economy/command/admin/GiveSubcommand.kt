package net.australmc.economy.command.admin

import net.australmc.economy.domain.Role.ADMIN
import net.australmc.economy.exception.catchCommandEconomyException
import net.australmc.economy.locale.LocaleProvider.getMappedMessage
import net.australmc.economy.locale.Message.ADMIN_GIVE_SUCCESS
import net.australmc.economy.service.money.givePlayerMoneyService
import net.australmc.economy.utils.formatMoney
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class GiveSubcommand : AdminSubcommandExecutor() {
    override val name = "give"
    override val alias = listOf("add", "dar", "adicionar")
    override val playerOnly = false
    override val correctUsage = "/eco give <jogador> <quantia>"
    override val description = "Adicionar uma quantia de dinheiro na conta de um jogador."
    override val allowedRoles = setOf(ADMIN)

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>) : Boolean {
        if(!validateAdminEconomyOperationSubcommand(sender, args)) return true

        val target = Bukkit.getPlayer(args[0])!!
        val amount = args[1].toDouble()

        catchCommandEconomyException(sender,
            { givePlayerMoneyService(target, amount) },
            { sender.sendMessage(
                getMappedMessage(
                    ADMIN_GIVE_SUCCESS, mapOf(
                        "jogador" to target.name,
                        "valor" to formatMoney(amount)
                    )
                )
            ) }
        )

        return true
    }

}