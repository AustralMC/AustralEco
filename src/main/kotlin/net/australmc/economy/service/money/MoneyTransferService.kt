package net.australmc.economy.service.money

import net.australmc.economy.domain.EconomicProfile
import net.australmc.economy.domain.Transaction
import net.australmc.economy.exception.EconomyErrorException
import net.australmc.economy.locale.LocaleProvider
import net.australmc.economy.locale.Message.CACHE_NOT_FOUND
import net.australmc.economy.locale.Message.INSUFFICIENT_MONEY
import net.australmc.economy.repository.EconomicProfileRepository
import net.australmc.economy.service.history.registerTransactionOnHistory
import org.bukkit.entity.Player

fun moneyTransferService(sender: Player, target: Player, amount: Double) {
    val senderProfile = EconomicProfileRepository[sender]
    val targetProfile = EconomicProfileRepository[target]

    validate(senderProfile, targetProfile, amount)

    senderProfile!!.transfer(targetProfile!!, amount)
    registerTransactionOnHistory(Transaction(senderProfile, targetProfile, amount))
}

private fun validate(senderProfile: EconomicProfile?, targetProfile: EconomicProfile?, amount: Double) {
    if(senderProfile == null || targetProfile == null) {
        throw EconomyErrorException(LocaleProvider[CACHE_NOT_FOUND])
    }

    if (!senderProfile.has(amount)) {
        throw EconomyErrorException(LocaleProvider[INSUFFICIENT_MONEY])
    }
}