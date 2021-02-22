package net.australmc.economy

import net.australmc.core.config.mapper.ConfigMapper
import net.australmc.economy.data.DataManager
import net.australmc.economy.domain.Currency
import net.australmc.economy.domain.DEFAULT_CURRENCY
import net.australmc.economy.hook.VaultHook
import net.australmc.economy.locale.LocaleProvider
import net.australmc.economy.utils.registerAllCommands
import net.australmc.economy.utils.registerAllListeners
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger

class AustralEco : JavaPlugin() {

    companion object {
        @JvmStatic
        lateinit var instance: AustralEco
            private set

        @JvmStatic
        lateinit var log: Logger
            private set

        @JvmStatic
        lateinit var dataManager: DataManager
            private set

        @JvmStatic
        lateinit var currency: Currency
    }

    override fun onEnable() {
        instance = this
        log = this.logger
        dataManager = DataManager(this)

        setupConfig()
        registerEconomyService()

        registerAllCommands(this)
        registerAllListeners(this)
    }

    private fun setupConfig() {
        if(!dataFolder.exists()) {
            dataFolder.mkdir()
        }

        saveDefaultConfig()
        config.options().copyDefaults(true)
        saveConfig()

        LocaleProvider.setConfig(config)
        currency = ConfigMapper.mapToObject(config.getConfigurationSection("Info-Moeda"), Currency::class.java)
            ?: DEFAULT_CURRENCY
    }

    private fun registerEconomyService() {
        if(Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            logger.info("Vault found! Registering as an Economy service...")

            val vaultHook = VaultHook()
            Bukkit.getServicesManager().register(Economy::class.java, vaultHook, this, ServicePriority.Normal)

        }
    }

}