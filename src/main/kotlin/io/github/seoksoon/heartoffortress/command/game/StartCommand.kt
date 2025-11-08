package io.github.seoksoon.heartoffortress.command.game

import io.github.seoksoon.heartoffortress.command.SubCommand
import io.github.seoksoon.heartoffortress.game.GameManager
import io.github.seoksoon.heartoffortress.util.MessageUtil
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StartCommand(private val gameManager: GameManager) : SubCommand {

    override fun onSubCommand(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            MessageUtil.send(sender, "&c이 명령어는 플레이어만 사용할 수 있습니다.")
            return true
        }

        if (!sender.isOp) {
            MessageUtil.send(sender, "&c관리자만 사용할 수 있습니다.")
            return true
        }

        if (gameManager.state.name != "WAITING") {
            MessageUtil.send(sender, "&c이미 게임이 진행 중입니다!")
            return true
        }

        gameManager.startCountdown()
        MessageUtil.send(sender, "&a게임 시작 카운트다운이 시작되었습니다!")
        return true
    }
}