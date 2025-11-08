import io.github.seoksoon.heartoffortress.command.SubCommand
import io.github.seoksoon.heartoffortress.game.TeamManager
import io.github.seoksoon.heartoffortress.util.MessageUtil
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RandomTeamCommand : SubCommand {
    override fun onSubCommand(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            MessageUtil.send(sender, "&c이 명령어는 플레이어만 사용할 수 있습니다.")
            return true
        }

        TeamManager.joinRandom(sender)
        return true
    }
}
