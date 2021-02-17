package net.australmc.economy.domain

enum class Role(val permissionNode: String) {
    ADMIN("australeco.admin"),
    MODERATOR("australeco.moderator"),
    PLAYER("australeco.player")
}