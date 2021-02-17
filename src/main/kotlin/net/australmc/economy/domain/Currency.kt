package net.australmc.economy.domain

import net.australmc.core.annotations.config.mapper.ConfigField

val DEFAULT_CURRENCY = Currency("Coin", "Coins", "$", "CNI")

data class Currency(
    @ConfigField("Nome") val name: String,
    @ConfigField("Plural") val plural:  String,
    @ConfigField("Simbolo") val symbol: String,
    @ConfigField("ISO") val iso: String
) {
    constructor() : this("", "", "", "")
}
