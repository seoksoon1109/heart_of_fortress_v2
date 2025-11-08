package io.github.seoksoon.heartoffortress.command

import io.github.seoksoon.heartoffortress.command.team.TeamCommand
import io.github.seoksoon.heartoffortress.game.GameManager
import io.github.seoksoon.heartoffortress.util.MessageUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class HofCommand(private val gameManager: GameManager) : CommandExecutor, TabCompleter {

    private val subCommands: Map<String, SubCommand> = mapOf(
        "start" to StartCommand(gameManager),
        "stop" to StopCommand(gameManager),
        "team"  to TeamCommand()
    )

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (args.isEmpty()) {
            MessageUtil.send(sender, "&e사용법: /hof <${subCommands.keys.joinToString("|")}>")
            return true
        }

        val sub = subCommands[args[0].lowercase()]
        if (sub == null) {
            MessageUtil.send(sender, "&c알 수 없는 명령어입니다. /hof <${subCommands.keys.joinToString("|")}>")
            return true
        }

        return sub.onSubCommand(sender, args.drop(1).toTypedArray())
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String> {
        return when (args.size) {
            0, 1 -> {
                val prefix = if (args.isEmpty()) "" else args[0].lowercase()
                subCommands.keys
                    .filter { it.startsWith(prefix, ignoreCase = true) }
                    .sorted()
            }

            else -> {
                val sub = subCommands[args[0].lowercase()] ?: return emptyList()
                sub.onTabComplete(sender, args.drop(1).toTypedArray())
            }
        }
    }
}
