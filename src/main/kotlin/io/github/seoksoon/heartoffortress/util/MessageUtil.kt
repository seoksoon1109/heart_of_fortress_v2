package io.github.seoksoon.heartoffortress.util

import io.github.seoksoon.heartoffortress.HeartOfFortressPlugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object MessageUtil {

    private val legacySerializer = LegacyComponentSerializer.legacySection()
    private val prefix = Component.text("[HOF] ", NamedTextColor.GOLD)
    private val logger by lazy { HeartOfFortressPlugin.instance.logger }

    fun broadcast(message: String) {
        val component = prefix.append(legacySerializer.deserialize(message))
        Bukkit.broadcast(component)
    }

    fun send(player: Player, message: String) {
        val component = prefix.append(legacySerializer.deserialize(message))
        player.sendMessage(component)
    }

    fun send(sender: CommandSender, message: String) {
        val component = prefix.append(legacySerializer.deserialize(message))
        sender.sendMessage(component)
    }

    fun log(message: String) {
        logger.info("[HOF] $message")
    }

    fun warn(message: String) {
        logger.warning("[HOF] $message")
    }

    fun debug(message: String) {
        val plugin = HeartOfFortressPlugin.instance
        if (plugin.config.getBoolean("debug", false)) {
            logger.info("[HOF:DEBUG] $message")
        }
    }

    fun error(message: String, exception: Throwable? = null) {
        if (exception != null) {
            logger.severe("[HOF] $message\n${exception.stackTraceToString()}")
        } else {
            logger.severe("[HOF] $message")
        }
    }
}
