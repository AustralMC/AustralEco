package net.australmc.economy.service.money

import net.australmc.economy.domain.EconomicProfile
import net.australmc.economy.exception.EconomyErrorException
import net.australmc.economy.locale.LocaleProvider
import net.australmc.economy.locale.Message.ADMIN_ERROR_CACHE
import net.australmc.economy.locale.Message.INVALID_AMOUNT
import net.australmc.economy.repository.EconomicProfileRepository
import org.bukkit.entity.Player

fun givePlayerMoneyService(player: Player, amount: Double) {
    val profile = EconomicProfileRepository[player]
    validate(profile, amount)

    profile?.give(amount)
}

private fun validate(profile: EconomicProfile?, amount: Double) {
    if(profile == null) throw EconomyErrorException(LocaleProvider[ADMIN_ERROR_CACHE])

    if(amount <= 0) throw EconomyErrorException(LocaleProvider[INVALID_AMOUNT])

    if((profile.balance?.plus(amount))!! < 0) throw EconomyErrorException(LocaleProvider[INVALID_AMOUNT])
}