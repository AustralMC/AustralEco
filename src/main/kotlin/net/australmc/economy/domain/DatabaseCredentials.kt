package net.australmc.economy.domain

import net.australmc.core.annotations.config.mapper.ConfigField

data class DatabaseCredentials(
    @ConfigField("Host") val host: String,
    @ConfigField("Porta") val port: Int,
    @ConfigField("Database") val database: String,
    @ConfigField("Usuario") val username: String,
    @ConfigField("Senha") val password: String,
    @ConfigField("Parametros") val parameters: String,
)