package net.australmc.economy.data

import net.australmc.economy.data.sources.DataSource
import net.australmc.economy.data.sources.FlatfileDataSource
import net.australmc.economy.data.sources.MySQLDataSource
import net.australmc.economy.domain.EconomicProfile
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class DataManager(plugin: JavaPlugin) {
    val source: DataSource<UUID, EconomicProfile> =
        if(plugin.config.getBoolean("Database.Use")) MySQLDataSource(plugin) else FlatfileDataSource(plugin)

    init {
        plugin.logger.info("Data source in use: ${source.name}")
    }
}