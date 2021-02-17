package net.australmc.economy.service.money

import net.australmc.economy.repository.EconomicProfileRepository
import org.bukkit.entity.Player

fun getPlayerBalanceService(player: Player) : Double = EconomicProfileRepository[player]?.balance ?: 0.00