package net.australmc.economy.utils

import net.australmc.economy.AustralEco.Companion.currency

fun getCurrencyNameByBalance(balance: Double) : String {
    return if(balance == 1.0) currency.name else currency.plural
}