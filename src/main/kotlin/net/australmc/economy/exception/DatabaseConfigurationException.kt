package net.australmc.economy.exception

class DatabaseConfigurationException(
    override val message: String = "Cannot parse database connection configuration section."
) : RuntimeException() {
}