package net.australmc.economy.exception

class EconomyErrorException(
    override val message: String,
) : RuntimeException() {
}