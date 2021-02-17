package net.australmc.economy.service.money

import net.australmc.economy.domain.Transaction
import net.australmc.economy.locale.LocaleProvider
import net.australmc.economy.locale.Message.CACHE_NOT_FOUND
import net.australmc.economy.locale.Message.INSUFFICIENT_MONEY
import net.australmc.economy.repository.EconomicProfileRepository
import net.australmc.economy.service.history.registerTransactionOnHistory
import org.bukkit.entity.Player

fun moneyTransferService(sender: Player, target: Player, amount: Double) : Boolean {
    val senderProfile = EconomicProfileRepository[sender]
    val targetProfile = EconomicProfileRepository[target]

    if(senderProfile == null || targetProfile == null) {
        sender.sendMessage(LocaleProvider[CACHE_NOT_FOUND])
        return false
    }

    if (senderProfile.has(amount)) {
        senderProfile.transfer(targetProfile, amount)
        registerTransactionOnHistory(Transaction(senderProfile, targetProfile, amount))
    } else {
        sender.sendMessage(LocaleProvider[INSUFFICIENT_MONEY])
    }

    return true
}