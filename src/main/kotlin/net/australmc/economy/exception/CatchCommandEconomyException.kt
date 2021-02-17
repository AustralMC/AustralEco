package net.australmc.economy.exception

import net.australmc.economy.locale.LocaleProvider
import net.australmc.economy.locale.Message.UNEXPECTED_ERROR
import org.bukkit.command.CommandSender
import java.util.function.Consumer
import java.util.function.Supplier

fun catchCommandEconomyException(sender: CommandSender, routine: Runnable, onSuccess: Runnable){
    try {
        routine.run()
        onSuccess.run()
    } catch(exception: EconomyErrorException) {
        sender.sendMessage(exception.message)
    } catch (otherExceptions: Exception) {
        sender.sendMessage(LocaleProvider[UNEXPECTED_ERROR])
    }
}

fun <T> catchCommandEconomyException(sender: CommandSender, routine: Supplier<T>, onSuccess: Consumer<T>) {
    try {
        onSuccess.accept(routine.get())
    } catch(exception: EconomyErrorException) {
        sender.sendMessage(exception.message)
    } catch (otherExceptions: Exception) {
        sender.sendMessage(LocaleProvider[UNEXPECTED_ERROR])
    }
}