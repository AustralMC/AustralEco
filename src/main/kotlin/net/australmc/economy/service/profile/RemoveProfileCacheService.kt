package net.australmc.economy.service.profile

import net.australmc.economy.AustralEco.Companion.dataManager
import net.australmc.economy.repository.EconomicProfileRepository
import org.bukkit.entity.Player

fun removeProfileCacheService(player: Player) {
    val profile = EconomicProfileRepository[player]

    if(profile != null) {
        dataManager.source.save(player.uniqueId, profile, null)
        EconomicProfileRepository.remove(player)
    }
}