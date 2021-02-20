package net.australmc.economy.data.sources

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import net.australmc.core.config.mapper.ConfigMapper
import net.australmc.economy.AustralEco
import net.australmc.economy.domain.DatabaseCredentials
import net.australmc.economy.domain.EconomicProfile
import net.australmc.economy.domain.Transaction
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Consumer

class MySQLDataSource(private val plugin: JavaPlugin) : DataSource<UUID, EconomicProfile>() {

    override val name = "MySQL"
    private val credentials: DatabaseCredentials = ConfigMapper.mapToObject(
        plugin.config.getConfigurationSection("Database"),
        DatabaseCredentials::class.java) ?: throw RuntimeException()
    private val pool: HikariDataSource

    init {
        val minimumConnections = (Bukkit.getMaxPlayers() / 2).coerceAtLeast(1)
        val maximumConnections = minimumConnections * 2
        val connectionTimeout: Long = 10000

        val connectionConfig = HikariConfig()
        connectionConfig.jdbcUrl =
            "jdbc:mysql://${credentials.host}:${credentials.port}/${credentials.database}${credentials.parameters}"
        connectionConfig.driverClassName = "com.mysql.jdbc.Driver"
        connectionConfig.username = credentials.username
        connectionConfig.password = credentials.password
        connectionConfig.minimumIdle = minimumConnections
        connectionConfig.maximumPoolSize = maximumConnections
        connectionConfig.connectionTimeout = connectionTimeout

        pool = HikariDataSource(connectionConfig)
        setupTables()
        pool.connection
    }

    override fun find(key: UUID, callback: BiConsumer<UUID, EconomicProfile?>) {
        val statement = pool.connection.prepareStatement("SELECT * FROM australeco_account")

        executeQuery(statement) {
            if(it.first()) {
                callback.accept(key, EconomicProfile(key, it.getString("owner_nickname"), it.getDouble("balance")))
            }
        }
    }

    override fun findAll(callback: Consumer<List<EconomicProfile>>) {
        val statement = pool.connection.prepareStatement("SELECT * FROM australeco_account")

        executeQuery(statement, getListQueryConsumer(callback))
    }

    override fun findAllBy(criteria: String, ascOrder: Boolean, callback: Consumer<List<EconomicProfile>>) {
        val order = if(ascOrder) "ASC" else "DESC"
        val statement = pool.connection.prepareStatement("SELECT * FROM australeco_account ORDER BY ?1 $order")
        statement.setString(1, criteria)

        executeQuery(statement, getListQueryConsumer(callback))
    }

    override fun findFirstBy(
        limit: Long, criteria: String, ascOrder: Boolean, callback: Consumer<List<EconomicProfile>>) {

        val order = if(ascOrder) "ASC" else "DESC"
        val statement = pool.connection.prepareStatement("SELECT * FROM australeco_account ORDER BY ?1 $order LIMIT ?2")
        statement.setString(1, criteria)
        statement.setLong(2, limit)

        executeQuery(statement, getListQueryConsumer(callback))
    }

    override fun save(key: UUID, value: EconomicProfile, callback: BiConsumer<UUID, EconomicProfile>?) {
        val statement = pool.connection.prepareStatement(
            "UPDATE australeco_account SET owner_nickname = ?1, balance = ?2 WHERE uuid = ?3")
        statement.setString(1, value.ownerName)
        statement.setDouble(2, value.balance!!)
        statement.setString(3, key.toString())
        executeUpdate(statement)

        callback?.accept(key, value)

    }

    override fun delete(key: UUID, callback: Consumer<UUID>?) {
        val statement = pool.connection.prepareStatement("DELETE FROM australeco_account WHERE uuid = ?1")
        statement.setString(1, key.toString())
        executeUpdate(statement)

        callback?.accept(key)
    }

    override fun onDisable() {
        pool.close()
    }

    fun saveHistory(transaction: Transaction) {
        fun selectAccountByUUID(uuid: String) = "SELECT id FROM australeco_account WHERE uuid = $uuid"

         val statement = pool.connection.prepareStatement(
             "INSERT INTO australeco_history (payer_id, receiver_id, amount) " +
             "VALUES (${selectAccountByUUID("?1")}, ${selectAccountByUUID("?2")}, ?3)"
         )
        statement.setString(1, transaction.payer.ownerUUID.toString())
        statement.setString(2, transaction.receiver.ownerUUID.toString())
        statement.setDouble(3, transaction.amount)

        executeUpdate(statement)
    }

    private fun setupTables() {
        val accountTableCreationStatement = pool.connection.prepareStatement(
            "CREATE TABLE IF NOT EXISTS australeco_account (" +
                    "id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                    "uuid VARCHAR(36) NOT NULL, " +
                    "owner_nickname VARCHAR(16) NOT NULL, " +
                    "balance DOUBLE NOT NULL DEFAULT 0, " +
                    "CONSTRAINT uk_auseco_acc_uuid UNIQUE KEY (uuid), " +
                    "CONSTRAINT uk_auseco_acc_nick UNIQUE KEY (owner_nickname)" +
            "); ")
        executeUpdate(accountTableCreationStatement)

        val historyTableCreationStatement = pool.connection.prepareStatement(
            "CREATE TABLE IF NOT EXISTS australeco_history (" +
                    "id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                    "payer_id BIGINT UNSIGNED NOT NULL, " +
                    "receiver_id BIGINT UNSIGNED NOT NULL, " +
                    "amount DOUBLE NOT NULL, " +
                    "CONSTRAINT fk_auseco_hist_payer FOREIGN KEY (payer_id) REFERENCES australeco_account(id), " +
                    "CONSTRAINT fk_auseco_hist_receiver FOREIGN KEY (receiver_id) REFERENCES australeco_account(id)" +
            ");")
        executeUpdate(historyTableCreationStatement)
    }

    private fun getListQueryConsumer(callback: Consumer<List<EconomicProfile>>) = Consumer<ResultSet> {
        val profiles = mutableListOf<EconomicProfile>()

        do {
            profiles.add(
                EconomicProfile(
                    UUID.fromString(it.getString("uuid")),
                    it.getString("owner_nickname"),
                    it.getDouble("balance")
                )
            )
        } while(it.next())

        callback.accept(profiles)
    }

    private fun executeQuery(statement: PreparedStatement, callback: Consumer<ResultSet>) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            var result: ResultSet? = null
            val connection = statement.connection
            try {
                result = statement.executeQuery()
            } catch(exception: SQLException) {
                AustralEco.log.severe("Error while executing database query routine:")
                exception.printStackTrace()
            } finally {
                if(result != null) {
                    callback.accept(result)

                    statement.close()
                    pool.evictConnection(connection)
                    result.close()
                }
            }
        })
    }

    private fun executeUpdate(statement: PreparedStatement) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            val connection = statement.connection
            try {
                statement.executeUpdate()
            } catch(exception: SQLException) {
                AustralEco.log.severe("Error while executing database update routine:")
                exception.printStackTrace()
            } finally {
                statement.close()
                pool.evictConnection(connection)
            }
        })
    }

}