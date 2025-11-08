package io.github.seoksoon.heartoffortress.command.team

import io.github.seoksoon.heartoffortress.command.SubCommand
import io.github.seoksoon.heartoffortress.game.TeamManager
import io.github.seoksoon.heartoffortress.util.MessageUtil
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class JoinTeamCommand : SubCommand {
    override fun onSubCommand(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            MessageUtil.send(sender, "&c이 명령어는 플레이어만 사용할 수 있습니다.")
            return true
        }

        if (args.isEmpty()) {
            MessageUtil.send(sender, "&e사용법: /hof team join <red|blue>")
            return true
        }

        TeamManager.joinTeam(sender, args[0])
        return true
    }
}
