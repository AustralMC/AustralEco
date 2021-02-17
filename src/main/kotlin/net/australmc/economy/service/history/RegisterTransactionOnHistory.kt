package net.australmc.economy.service.history

import net.australmc.economy.AustralEco.Companion.dataManager
import net.australmc.economy.domain.Transaction
import net.australmc.economy.data.sources.MySQLDataSource

fun registerTransactionOnHistory(transaction: Transaction) {
    val mysqlDataSource = dataManager.source as? MySQLDataSource

    mysqlDataSource?.saveHistory(transaction)
}