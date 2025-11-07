package io.github.seoksoon.heartoffortress

import org.bukkit.plugin.java.JavaPlugin

class HeartOfFortressPlugin : JavaPlugin() {
    companion object {
        lateinit var instance: HeartOfFortressPlugin
            private set

        /* lateinit var gameManager: GameManager
            private set

         */
    }

    override fun onEnable() {
        instance = this
        saveDefaultConfig()
        logger.info("💖 Heart of Fortress (Kotlin) enabled successfully!")
    }

    override fun onDisable() {
        logger.info("💔 Heart of Fortress (Kotlin) has been disabled.")
    }
}