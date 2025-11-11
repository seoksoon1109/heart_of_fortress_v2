package io.github.seoksoon.heartoffortress.listener

import io.github.seoksoon.heartoffortress.game.GameManager
import io.github.seoksoon.heartoffortress.game.SpawnManager
import io.github.seoksoon.heartoffortress.game.TeamManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

class PlayerRespawnListener(
    private val teamManager: TeamManager,
    private val spawnManager: SpawnManager,
    private val gameManager: GameManager
) : Listener {

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        val player = event.player

        if (gameManager.isRunning()) {
            val team = teamManager.getTeamOf(player)?.name ?: return
            val teamSpawn = spawnManager.getTeamSpawn(team)
            if (teamSpawn != null) {
                event.respawnLocation = teamSpawn
            }
            return
        }

        // ✅ 게임이 진행 중이 아니면 기본 리스폰
        val defaultSpawn = spawnManager.getDefaultSpawn()
        if (defaultSpawn != null) {
            event.respawnLocation = defaultSpawn
        }
    }
}
