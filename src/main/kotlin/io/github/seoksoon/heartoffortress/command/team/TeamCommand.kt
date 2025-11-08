package io.github.seoksoon.heartoffortress.command.team

import RandomTeamCommand
import io.github.seoksoon.heartoffortress.command.SubCommand
import io.github.seoksoon.heartoffortress.game.TeamManager
import io.github.seoksoon.heartoffortress.util.MessageUtil
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TeamCommand : SubCommand {
    private val subCommands: Map<String, SubCommand> = mapOf(
        "join" to JoinTeamCommand(),
        "random" to RandomTeamCommand(),
        "leave" to LeaveTeamCommand(),
        "list" to ListTeamCommand(),
        "reset" to ResetTeamCommand()
    )

    override fun onSubCommand(sender: CommandSender, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            MessageUtil.send(sender, "&e/team <join|random|leave|list|reset>")
            return true
        }

        val sub = subCommands[args[0].lowercase()]
        if (sub == null) {
            MessageUtil.send(sender, "&c알 수 없는 하위 명령어입니다.")
            return true
        }

        return sub.onSubCommand(sender, args.drop(1).toTypedArray())
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>): List<String> {
        return when (args.size) {
            1 -> subCommands.keys
                .filter { it.startsWith(args[0], ignoreCase = true) }
            2 -> {
                if (args[0].equals("join", true))
                    listOf("red", "blue").filter { it.startsWith(args[1], ignoreCase = true) }
                else emptyList()
            }
            else -> emptyList()
        }
    }
}
