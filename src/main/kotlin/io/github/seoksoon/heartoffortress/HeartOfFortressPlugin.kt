package io.github.seoksoon.heartoffortress

import org.bukkit.plugin.java.JavaPlugin

class HeartOfFortressPlugin : JavaPlugin() {

    override fun onEnable() {
        logger.info("💖 Heart of Fortress (Kotlin) enabled successfully!")
    }

    override fun onDisable() {
        logger.info("💔 Heart of Fortress (Kotlin) has been disabled.")
    }
}
