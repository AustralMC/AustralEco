package net.australmc.economy.locale

import net.australmc.economy.utils.replacePlaceholders
import org.bukkit.ChatColor.translateAlternateColorCodes
import org.bukkit.configuration.file.FileConfiguration
import java.util.stream.Collectors

object LocaleProvider {

    private const val MESSAGES_ROOT_PATH = "Mensagens"
    private const val PREFIXES_PATH = "${MESSAGES_ROOT_PATH}.Prefixos"
    private var config: FileConfiguration? = null

    fun setConfig(newConfig: FileConfiguration?) {
        config = newConfig
    }

    operator fun get(message: Message) : String {
        val text = getTextFromConfig("${MESSAGES_ROOT_PATH}.${message.path}")

        return replacePlaceholders(text, mapOf(
                Pair("prefixo-normal", getPrefix(MessagePrefix.NORMAL)),
                Pair("prefixo-erro", getPrefix(MessagePrefix.ERROR)),
        ))
    }

    fun getMappedMessage(message: Message, placeholdersMap: Map<String, String>) : String {
        val rawMessage = this[message]

        return replacePlaceholders(rawMessage, placeholdersMap)
    }

    fun getCorrectUsageMessage(example: String) = getMappedMessage(Message.CORRECT_USAGE, mapOf("exemplo" to example))

    fun getPrefix(prefix: MessagePrefix) = getTextFromConfig("${PREFIXES_PATH}.${prefix.path}")

    fun getList(message: Message) : List<String> {
        return config!!.getStringList(MESSAGES_ROOT_PATH + "." + message.path)
                .stream()
                .map { line: String? -> translateAlternateColorCodes('&', line!!) }
                .collect(Collectors.toList())
    }

    private fun getTextFromConfig(path: String) =
            translateAlternateColorCodes('&', config?.getString(path) ?: "")
}