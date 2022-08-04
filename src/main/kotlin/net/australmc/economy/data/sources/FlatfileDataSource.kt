package net.australmc.economy.data.sources

import net.australmc.core.config.mapper.ConfigMapper
import net.australmc.core.config.mapper.ConfigMapper.mapToObject
import net.australmc.economy.AustralEco.Companion.log
import net.australmc.economy.annotation.SQLColumn
import net.australmc.economy.domain.EconomicProfile
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.stream.Collectors.toList
import java.util.stream.Stream

private const val FILE_NAME = "data.yml"

class FlatfileDataSource(plugin: JavaPlugin) : DataSource<UUID, EconomicProfile>() {

    override val name = "Flatfile"
    private val fileConfiguration: FileConfiguration
    private val dataFile: File

    init {
        val dataFolder = plugin.dataFolder
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }

        dataFile = File(dataFolder, FILE_NAME)
        fileConfiguration = YamlConfiguration.loadConfiguration(dataFile)

        saveFile()
    }

    override fun find(key: UUID, callback: BiConsumer<UUID, EconomicProfile?>) {
        val section = getProfileSection(key.toString())

        val profile: EconomicProfile? = mapToObject(section, EconomicProfile::class.java)
        profile?.ownerUUID = key

        callback.accept(key, profile)
    }

    override fun findAll(callback: Consumer<List<EconomicProfile>>) {
        callback.accept(findAllStream().collect(toList()))
    }

    override fun findAllBy(criteria: String, ascOrder: Boolean, callback: Consumer<List<EconomicProfile>>) {
        callback.accept(sortedStream(criteria, ascOrder).collect(toList()))
    }

    override fun findFirstBy(
        limit: Long,
        criteria: String,
        ascOrder: Boolean,
        callback: Consumer<List<EconomicProfile>>
    ) {
        val profiles = sortedStream(criteria, ascOrder)
            .limit(limit)
            .collect(toList())
        callback.accept(profiles)
    }

    override fun insert(key: UUID, value: EconomicProfile, callback: BiConsumer<UUID, EconomicProfile>?) =
        save(key, value, callback) // No need to create special treatment for insert or save in flatfile

    override fun save(key: UUID, value: EconomicProfile, callback: BiConsumer<UUID, EconomicProfile>?) {
        val section = getOrCreateProfileSection(key.toString())

        ConfigMapper.mapToSection(section, value)

        saveFile()

        callback?.accept(key, value)
    }

    override fun delete(key: UUID, callback: Consumer<UUID>?) {
        val uuidString = key.toString()
        if (fileConfiguration.contains(uuidString)) {
            fileConfiguration[uuidString] = null
        }
        saveFile()
        callback?.accept(key)
    }

    override fun onDisable() = saveFile()

    private fun findAllStream(): Stream<EconomicProfile> {
        return fileConfiguration.getKeys(false).stream()
            .map { key: String ->

                val economicProfile = mapToObject(getProfileSection(key), EconomicProfile::class.java)
                economicProfile?.ownerUUID = UUID.fromString(key)

                return@map economicProfile
            }
    }

    private fun sortedStream(criteria: String?, ascOrder: Boolean): Stream<EconomicProfile> {
        val comparator = getCriteriaComparatorByTableName(criteria)

        return findAllStream().sorted(if(ascOrder) comparator.reversed() else comparator)
    }

    private fun getOrCreateProfileSection(uuidString: String): ConfigurationSection {
        return getProfileSection(uuidString) ?: fileConfiguration.createSection(uuidString)
    }

    private fun getProfileSection(uuidString: String): ConfigurationSection? {
        return fileConfiguration.getConfigurationSection(uuidString)
    }

    private fun saveFile() {
        try {
            fileConfiguration.save(dataFile)
        } catch (exception: IOException) {
            log.severe("Error saving $FILE_NAME file!")
            exception.printStackTrace()
        }
    }

    private fun getCriteriaComparatorByTableName(criteria: String?): Comparator<EconomicProfile> {
        var criteriaComparator: Comparator<EconomicProfile>? = null

        try {
            val criteriaMethod = EconomicProfile::class.java
                .fields.filter {
                    return@filter if(it.isAnnotationPresent(SQLColumn::class.java)) {
                         it.getAnnotation(SQLColumn::class.java).name == criteria
                    } else false
                }.firstOrNull()


            criteriaComparator = Comparator.comparingDouble { profile: EconomicProfile ->
                try {
                    return@comparingDouble criteriaMethod?.getDouble(profile)!!
                } catch (ignored: Exception) {
                    return@comparingDouble profile.balance!!
                }
            }
        } catch (ignored: Exception) {}

        return criteriaComparator ?: Comparator.comparingDouble { it.balance!! }
    }
}