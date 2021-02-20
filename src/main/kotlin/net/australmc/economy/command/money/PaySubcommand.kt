package net.australmc.economy.command.money

import net.australmc.economy.command.SubcommandExecutor
import net.australmc.economy.exception.catchCommandEconomyException
import net.australmc.economy.locale.LocaleProvider
import net.australmc.economy.locale.LocaleProvider.getCorrectUsageMessage
import net.australmc.economy.locale.LocaleProvider.getMappedMessage
import net.australmc.economy.locale.Message.COMMAND_MONEY_PAY_RECEIVED
import net.australmc.economy.locale.Message.COMMAND_MONEY_PAY_SENT
import net.australmc.economy.locale.Message.INVALID_AMOUNT
import net.australmc.economy.locale.Message.PARAMETER_MUST_BE_NUMBER
import net.australmc.economy.locale.Message.PLAYER_NOT_FOUND
import net.australmc.economy.service.money.moneyTransferService
import net.australmc.economy.utils.formatMoney
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PaySubcommand : SubcommandExecutor() {
    override val name: String = "pay"
    override val alias: List<String> = listOf("pagar", "transferir")
    override val correctUsage: String = "/money pay <jogador> <quantia>"
    override val description: String = "Enviar dinheiro a outro jogador."
    override val playerOnly: Boolean = true

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>) : Boolean {
        if(validate(sender, args)) {
            val player = sender as Player
            val target = Bukkit.getPlayer(args[0])!!
            val amount = args[1].toDouble()

            catchCommandEconomyException(
                player,
                { moneyTransferService(player, target, amount) },
                {
                    sender.sendMessage(
                        getMappedMessage(COMMAND_MONEY_PAY_SENT, mapOf(
                            "alvo" to target.name,
                            "valor" to formatMoney(amount)
                        )))

                    target.sendMessage(
                        getMappedMessage(COMMAND_MONEY_PAY_RECEIVED, mapOf(
                            "origem" to sender.name,
                            "valor" to formatMoney(amount)
                        )))
                })
        }

        return true
    }

    private fun validate(sender: CommandSender, args: Array<String>) : Boolean {
        if(args.size < 2) {
            sender.sendMessage(getCorrectUsageMessage(this.correctUsage))
            return false
        }

        val target = Bukkit.getPlayer(args[0])

        if(target == null) {
            sender.sendMessage(LocaleProvider[PLAYER_NOT_FOUND])
            return false
        }

        val value =  args[1]
        if(value.toDoubleOrNull() == null) {
            sender.sendMessage(getMappedMessage(PARAMETER_MUST_BE_NUMBER, mapOf("parametro" to "valor")))
            return false
        }

        if(value.toDouble() <= 0) {
            sender.sendMessage(LocaleProvider[INVALID_AMOUNT])
            return false
        }

        return true
    }

}