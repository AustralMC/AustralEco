package net.australmc.economy.hook

import net.australmc.economy.AustralEco
import net.australmc.economy.AustralEco.Companion.dataManager
import net.australmc.economy.domain.EconomicProfile
import net.australmc.economy.repository.EconomicProfileRepository
import net.australmc.economy.utils.FRACTIONAL_DIGITS
import net.australmc.economy.utils.formatMoney
import net.milkbowl.vault.economy.AbstractEconomy
import net.milkbowl.vault.economy.EconomyResponse
import net.milkbowl.vault.economy.EconomyResponse.ResponseType.FAILURE
import net.milkbowl.vault.economy.EconomyResponse.ResponseType.SUCCESS
import org.bukkit.Bukkit
import org.bukkit.entity.Player

private fun getPlayerNullSafe(name: String?): Player? {
    if(name == null) return null

    return Bukkit.getPlayer(name)
}

private fun getAccount(name: String?): EconomicProfile? {
    val player = getPlayerNullSafe(name)

    return if(player == null) null else EconomicProfileRepository[player]
}

private fun throwBankUnsupported(): Nothing = TODO("Banking is not supported")

private fun bankUnsupportedException(): EconomyResponse = throwBankUnsupported()

class VaultHook : AbstractEconomy() {

    override fun isEnabled(): Boolean = true

    override fun getName(): String = AustralEco.instance.description.name

    override fun hasBankSupport(): Boolean = false

    override fun fractionalDigits(): Int = FRACTIONAL_DIGITS

    override fun format(amount: Double): String = formatMoney(amount)

    override fun currencyNamePlural(): String = AustralEco.currency.plural

    override fun currencyNameSingular(): String = AustralEco.currency.name

    override fun hasAccount(playerName: String?): Boolean =getAccount(playerName) != null

    override fun hasAccount(playerName: String?, worldName: String?): Boolean = this.hasAccount(playerName)

    override fun getBalance(playerName: String?): Double = getAccount(playerName)?.balance ?: 0.0

    override fun getBalance(playerName: String?, world: String?): Double = this.getBalance(playerName)

    override fun has(playerName: String?, amount: Double): Boolean = getAccount(playerName)?.has(amount) ?: false

    override fun has(playerName: String?, worldName: String?, amount: Double): Boolean = this.has(playerName, amount)

    override fun withdrawPlayer(playerName: String?, amount: Double): EconomyResponse {
        val account = getAccount(playerName) ?: return EconomyResponse(amount, 0.0, FAILURE, null)

        return if(account.has(amount)) {
            account.take(amount)
            EconomyResponse(amount, account.balance!!, SUCCESS, null)
        } else
            EconomyResponse(amount, account.balance!!, FAILURE, null)
    }

    override fun withdrawPlayer(playerName: String?, worldName: String?, amount: Double): EconomyResponse =
        this.withdrawPlayer(playerName, amount)

    override fun depositPlayer(playerName: String?, amount: Double): EconomyResponse {
        val account = getAccount(playerName) ?: return EconomyResponse(amount, 0.0, FAILURE, null)

        account.give(amount)

        return EconomyResponse(amount, account.balance!!, SUCCESS, null)
    }

    override fun depositPlayer(playerName: String?, worldName: String?, amount: Double): EconomyResponse =
        this.depositPlayer(playerName, amount)

    override fun createPlayerAccount(playerName: String?): Boolean {
        val player = getPlayerNullSafe(playerName) ?: return false
        val profile = EconomicProfile(player.uniqueId, player.name, 0.0)

        EconomicProfileRepository[player] = profile

        dataManager.source.save(player.uniqueId, profile, null)

        return true
    }

    override fun createPlayerAccount(playerName: String?, worldName: String?): Boolean =
        this.createPlayerAccount(playerName)

    override fun createBank(name: String?, player: String?): EconomyResponse = bankUnsupportedException()

    override fun deleteBank(name: String?): EconomyResponse = bankUnsupportedException()

    override fun bankBalance(name: String?): EconomyResponse = bankUnsupportedException()

    override fun bankHas(name: String?, amount: Double): EconomyResponse = bankUnsupportedException()

    override fun bankWithdraw(name: String?, amount: Double): EconomyResponse = bankUnsupportedException()

    override fun bankDeposit(name: String?, amount: Double): EconomyResponse = bankUnsupportedException()

    override fun isBankOwner(name: String?, playerName: String?): EconomyResponse = bankUnsupportedException()

    override fun isBankMember(name: String?, playerName: String?): EconomyResponse = bankUnsupportedException()

    override fun getBanks(): MutableList<String> {
        throwBankUnsupported()
    }

}