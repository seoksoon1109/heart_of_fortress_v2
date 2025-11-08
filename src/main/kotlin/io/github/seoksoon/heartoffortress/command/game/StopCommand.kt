package io.github.seoksoon.heartoffortress.command.game

import io.github.seoksoon.heartoffortress.GameState
import io.github.seoksoon.heartoffortress.command.SubCommand
import io.github.seoksoon.heartoffortress.game.GameManager
import io.github.seoksoon.heartoffortress.util.MessageUtil
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StopCommand(private val gameManager: GameManager) : SubCommand {

    override fun onSubCommand(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            MessageUtil.send(sender, "&c이 명령어는 플레이어만 사용할 수 있습니다.")
            return true
        }

        if (!sender.isOp) {
            MessageUtil.send(sender, "&c관리자만 사용할 수 있습니다.")
            return true
        }

        if (gameManager.state == GameState.WAITING || gameManager.state == GameState.ENDED) {
            MessageUtil.send(sender, "&c아직 게임이 시작되지 않았습니다!")
            return true
        }
        gameManager.stopGame(true)
        return true
    }
}