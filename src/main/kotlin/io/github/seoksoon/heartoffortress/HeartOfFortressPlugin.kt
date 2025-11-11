package io.github.seoksoon.heartoffortress

import io.github.seoksoon.heartoffortress.command.CommandRegistry
import io.github.seoksoon.heartoffortress.game.GameManager
import io.github.seoksoon.heartoffortress.game.SpawnManager
import io.github.seoksoon.heartoffortress.game.TeamManager
import io.github.seoksoon.heartoffortress.gui.AbilitySelectGUI
import io.github.seoksoon.heartoffortress.gui.KillScoreboardManager
import io.github.seoksoon.heartoffortress.listener.PlayerRespawnListener
import org.bukkit.plugin.java.JavaPlugin

class HeartOfFortressPlugin : JavaPlugin() {

    companion object {
        lateinit var instance: HeartOfFortressPlugin
            private set

        lateinit var gameManager: GameManager
            private set

        lateinit var spawnManager: SpawnManager
            private set
    }

    override fun onEnable() {
        instance = this
        saveDefaultConfig()

        TeamManager.init(this)
        spawnManager = SpawnManager(this)
        spawnManager.loadTeamSpawns()
        gameManager = GameManager(this)

        server.pluginManager.registerEvents(AbilitySelectGUI, this)
        server.pluginManager.registerEvents(PlayerRespawnListener(this,TeamManager, spawnManager, gameManager), this)
        server.pluginManager.registerEvents(KillScoreboardManager(), this)

        CommandRegistry.registerAll(this, gameManager)
        logger.info("💖 Heart of Fortress (Kotlin) enabled successfully!")
    }

    override fun onDisable() {
        logger.info("💔 Heart of Fortress (Kotlin) has been disabled.")
    }
}
