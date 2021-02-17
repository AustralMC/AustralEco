package net.australmc.economy.locale

enum class Message(val path: String) {
    ADMIN_ERROR_CACHE("Comandos.Admin.Erro.Cache"),
    ADMIN_ERROR_INSUFICCIENT_MONEY("Comandos.Admin.Erro.Dinheiro-Insuficiente"),
    ADMIN_GIVE_SUCCESS("Comandos.Admin.Give.Sucesso"),
    ADMIN_SET_SUCCESS("Comandos.Admin.Set.Sucesso"),
    ADMIN_TAKE_SUCCESS("Comandos.Admin.Take.Sucesso"),
    CACHE_NOT_FOUND("Common.Nao-Encontrado-Cache"),
    COMMAND_MONEY_OTHER_BALANCE("Comandos.Money.Saldo-Outro-Jogador"),
    COMMAND_MONEY_OWN_BALANCE("Comandos.Money.Seu-Saldo"),
    CORRECT_USAGE("Common.Uso-Correto"),
    INSUFFICIENT_MONEY("Common.Dinheiro-Insuficiente"),
    INVALID_AMOUNT("Common.Quantia-Invalida"),
    NO_PERMISSION("Common.Sem-Permissao"),
    ONLY_INGAME_COMMAND("Common.Comando-Apenas-InGame"),
    PARAMETER_MUST_BE_NUMBER("Common.Valor-Precisa-Ser-Numero"),
    PLAYER_NOT_FOUND("Common.Jogador-Nao-Encontrado"),
    UNEXPECTED_ERROR("Common.Erro-Inesperado"),
}
