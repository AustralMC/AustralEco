package net.australmc.economy.event.listener

import net.australmc.economy.AustralEco.Companion.dataManager
import net.australmc.economy.service.profile.loadProfileService
import net.australmc.economy.service.profile.removeProfileCacheService
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class DataManagementEventListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        dataManager.source.find(player.uniqueId) { _, profile ->
            loadProfileService(player, profile)
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        removeProfileCacheService(event.player)
    }

}