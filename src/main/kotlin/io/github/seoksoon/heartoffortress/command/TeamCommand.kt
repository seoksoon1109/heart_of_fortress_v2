package io.github.seoksoon.heartoffortress.command.team

import io.github.seoksoon.heartoffortress.command.SubCommand
import io.github.seoksoon.heartoffortress.game.TeamManager
import io.github.seoksoon.heartoffortress.util.MessageUtil
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TeamCommand : SubCommand {

    override fun onSubCommand(sender: CommandSender, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            MessageUtil.send(sender, "&e/team <join|random|leave|list|reset>")
            return true
        }

        when (args[0].lowercase()) {
            "join" -> {
                if (sender !is Player) {
                    MessageUtil.send(sender, "&c이 명령어는 플레이어만 사용할 수 있습니다.")
                    return true
                }
                if (args.size < 2) {
                    MessageUtil.send(sender, "&e사용법: /hof team join <red|blue>")
                    return true
                }
                TeamManager.joinTeam(sender, args[1])
            }

            "random" -> {
                if (sender !is Player) {
                    MessageUtil.send(sender, "&c이 명령어는 플레이어만 사용할 수 있습니다.")
                    return true
                }
                TeamManager.joinRandom(sender)
            }

            "leave" -> {
                if (sender !is Player) {
                    MessageUtil.send(sender, "&c이 명령어는 플레이어만 사용할 수 있습니다.")
                    return true
                }
                TeamManager.leaveTeam(sender)
            }

            "list" -> {
                if (sender !is Player) {
                    MessageUtil.send(sender, "&c이 명령어는 플레이어만 사용할 수 있습니다.")
                    return true
                }
                TeamManager.listTeams(sender)
            }

            "reset" -> {
                if (!sender.isOp) {
                    MessageUtil.send(sender, "&c관리자만 사용할 수 있습니다.")
                    return true
                }
                if (sender !is Player) {
                    MessageUtil.send(sender, "&c이 명령어는 플레이어만 사용할 수 있습니다.")
                    return true
                }
                TeamManager.resetTeams(sender)
            }

            else -> MessageUtil.send(sender, "&c알 수 없는 하위 명령어입니다.")
        }

        return true
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>): List<String> {
        return when (args.size) {
            1 -> listOf("join", "random", "leave", "list", "reset")
                .filter { it.startsWith(args[0], ignoreCase = true) }

            2 -> when (args[0].lowercase()) {
                "join" -> listOf("red", "blue")
                    .filter { it.startsWith(args[1], ignoreCase = true) }
                else -> emptyList()
            }

            else -> emptyList()
        }
    }
}
