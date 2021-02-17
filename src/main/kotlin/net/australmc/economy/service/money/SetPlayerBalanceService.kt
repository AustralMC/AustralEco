package net.australmc.economy.service.money

import net.australmc.economy.exception.EconomyErrorException
import net.australmc.economy.locale.LocaleProvider
import net.australmc.economy.locale.Message.INVALID_AMOUNT
import net.australmc.economy.repository.EconomicProfileRepository
import org.bukkit.entity.Player

fun setPlayerBalanceService(player: Player, amount: Double) {
    validate(amount)

    EconomicProfileRepository[player]?.set(amount)
}

private fun validate(amount: Double) {
    if(amount <= 0) throw EconomyErrorException(LocaleProvider[INVALID_AMOUNT])
}