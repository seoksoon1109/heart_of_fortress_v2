package io.github.seoksoon.heartoffortress

import io.github.seoksoon.heartoffortress.command.CommandRegistry
import io.github.seoksoon.heartoffortress.game.GameManager
import io.github.seoksoon.heartoffortress.game.TeamManager
import org.bukkit.plugin.java.JavaPlugin

class HeartOfFortressPlugin : JavaPlugin() {
    companion object {
        lateinit var instance: HeartOfFortressPlugin
            private set

        lateinit var gameManager: GameManager
            private set
    }

    override fun onEnable() {
        instance = this
        gameManager = GameManager(this)
        TeamManager.init(this)
        saveDefaultConfig()
        CommandRegistry.registerAll(this, gameManager)
        logger.info("💖 Heart of Fortress (Kotlin) enabled successfully!")
    }

    override fun onDisable() {
        logger.info("💔 Heart of Fortress (Kotlin) has been disabled.")
    }
}