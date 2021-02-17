package net.australmc.economy.utils

import net.australmc.economy.AustralEco.Companion.currency
import java.text.DecimalFormat

const val FRACTIONAL_DIGITS = 2
private const val FORMAT_PATTERN = "#,##0.00"

fun formatMoney(value: Double) : String {
    val decimalFormat = DecimalFormat(FORMAT_PATTERN)

    return currency.symbol + decimalFormat.format(value)
}