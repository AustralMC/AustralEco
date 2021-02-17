package net.australmc.economy.command.money

import net.australmc.core.annotations.command.CommandClass
import net.australmc.economy.command.AbstractCommand
import net.australmc.economy.command.SubcommandExecutor.Companion.getSubcommand
import net.australmc.economy.command.SubcommandExecutor.Companion.isSubcommand
import net.australmc.economy.locale.LocaleProvider.getCorrectUsageMessage
import net.australmc.economy.locale.LocaleProvider.getMappedMessage
import net.australmc.economy.locale.Message.COMMAND_MONEY_OTHER_BALANCE
import net.australmc.economy.locale.Message.COMMAND_MONEY_OWN_BALANCE
import net.australmc.economy.service.money.getPlayerBalanceService
import net.australmc.economy.utils.formatMoney
import net.australmc.economy.utils.getCurrencyNameByBalance
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandClass(name = "money")
class MoneyCommand : CommandExecutor, AbstractCommand {

    companion object {
        internal val subCommands = setOf(
            PaySubcommand(),
            HelpSubcommand(),
        )
    }

    override val name: String = "money"
    override val alias: List<String> = listOf("dinheiro", "saldo")
    override val correctUsage: String = "/money [jogador]"
    override val description: String = "Ver seu saldo ou o saldo de outro jogador."
    override val playerOnly: Boolean = false

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>) : Boolean {
        if(args.isNotEmpty()) {
            val firstArg = args[0]

            if(isSubcommand(firstArg, subCommands)) {
                val subCommandArgs = args.drop(1).toTypedArray()
                return getSubcommand(firstArg, subCommands)!!
                    .execute(sender, cmd, firstArg, subCommandArgs)
            }

            val targetPlayer = Bukkit.getPlayer(firstArg)
            if(targetPlayer != null) {
                sendOtherPlayerBalanceInfo(sender, targetPlayer)
                return true
            }
        }

        if(sender is Player) {
            sendOwnBalanceInfo(sender)
        } else {
            sender.sendMessage(getCorrectUsageMessage(correctUsage))
        }

        return true
    }

    private fun sendOwnBalanceInfo(sender: Player) {
        val balance = getPlayerBalanceService(sender)
        sender.sendMessage(
            getMappedMessage(
                COMMAND_MONEY_OWN_BALANCE,
                mapOf(
                    "nome-moeda" to getCurrencyNameByBalance(balance),
                    "valor" to formatMoney(balance),
                )
            )
        )
    }

    private fun sendOtherPlayerBalanceInfo(sender: CommandSender, target: Player) {
        val balance = getPlayerBalanceService(target)
        sender.sendMessage(
            getMappedMessage(
                COMMAND_MONEY_OTHER_BALANCE,
                mapOf(
                    "alvo" to target.name,
                    "nome-moeda" to getCurrencyNameByBalance(balance),
                    "valor" to formatMoney(balance),
                )
            )
        )
    }
}