package net.australmc.economy.repository

import net.australmc.economy.domain.EconomicProfile
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

object EconomicProfileRepository {
    private val profilesMap = hashMapOf<OfflinePlayer, EconomicProfile>()

    operator fun get(player: Player) = profilesMap[player]

    operator fun set(player: Player, economicProfile: EconomicProfile) {
        profilesMap[player] = economicProfile
    }

    fun exists(player: Player) = profilesMap.contains(player)

    fun remove(player: Player) = profilesMap.remove(player)

}