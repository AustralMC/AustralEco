package net.australmc.economy.domain

import io.netty.util.internal.StringUtil.EMPTY_STRING
import net.australmc.core.annotations.config.mapper.ConfigField
import net.australmc.economy.annotation.SQLTable
import java.util.*
import java.util.UUID.randomUUID


class EconomicProfile(
    @SQLTable(name = "uuid") var ownerUUID: UUID,
    @SQLTable(name = "owner_nickname") @ConfigField("Owner-Name") val ownerName: String?,
    @SQLTable(name = "balance") @ConfigField("Balance") var balance: Double? = 0.0,
) {

    constructor() : this(randomUUID(), EMPTY_STRING, 0.0)

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