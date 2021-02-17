package net.australmc.economy.command

interface AbstractCommand {
    val name: String
    val alias: List<String>
    val correctUsage: String
    val description: String
    val playerOnly: Boolean
}