package net.australmc.economy.domain

import net.australmc.core.annotations.config.mapper.ConfigField
import net.australmc.economy.annotation.SQLColumn
import org.apache.commons.lang3.StringUtils.EMPTY
import java.util.*
import java.util.UUID.randomUUID


class EconomicProfile(
    @SQLColumn(name = "uuid") var ownerUUID: UUID,
    @SQLColumn(name = "owner_nickname") @ConfigField("Owner-Name") val ownerName: String?,
    @SQLColumn(name = "balance") @ConfigField("Balance") var balance: Double? = 0.0,
) {

    constructor() : this(randomUUID(), EMPTY, 0.0)

    fun has(amount: Double): Boolean {
        return this.balance!! >= amount
    }

    fun take(amount: Double) {
        this.balance = this.balance?.minus(amount)
    }

    fun give(amount: Double) {
        this.balance = this.balance?.plus(amount)
    }

    fun set(value: Double) {
        balance = value
    }

    fun transfer(target: EconomicProfile, amount: Double) {
        val amountToSend = this.balance!!.coerceAtMost(amount)

        target.give(amountToSend)
        this.take(amountToSend)
    }
}