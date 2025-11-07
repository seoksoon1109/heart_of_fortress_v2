package io.github.seoksoon.heartoffortress.command

import org.bukkit.command.CommandSender

interface SubCommand {
    fun onSubCommand(sender: CommandSender, args: Array<out String>): Boolean
    fun onTabComplete(sender: CommandSender, args: Array<out String>): List<String> = emptyList()
}
