package net.australmc.economy.service.profile

import net.australmc.economy.AustralEco.Companion.dataManager
import net.australmc.economy.domain.EconomicProfile
import net.australmc.economy.repository.EconomicProfileRepository
import org.bukkit.entity.Player

fun loadProfileService(player: Player, economicProfile: EconomicProfile?) {
    val efectiveProfile: EconomicProfile = economicProfile ?: createNewProfile(player)

    EconomicProfileRepository[player] = efectiveProfile
}

private fun createNewProfile(player: Player) : EconomicProfile {
    val newProfile = EconomicProfile(player.uniqueId, player.name, 0.00)
    dataManager.source.save(player.uniqueId, newProfile, null)

    return newProfile
}