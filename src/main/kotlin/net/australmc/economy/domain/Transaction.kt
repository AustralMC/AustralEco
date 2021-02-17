package net.australmc.economy.domain

data class Transaction(
    val id: Long?,
    val payer: EconomicProfile,
    val receiver: EconomicProfile,
    val amount: Double
) {
    constructor(payer: EconomicProfile, receiver: EconomicProfile, amount: Double) :
            this(null, payer, receiver, amount)
}
