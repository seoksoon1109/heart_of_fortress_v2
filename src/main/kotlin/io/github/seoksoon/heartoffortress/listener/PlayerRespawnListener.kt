package io.github.seoksoon.heartoffortress.listener

import io.github.seoksoon.heartoffortress.game.GameManager
import io.github.seoksoon.heartoffortress.game.SpawnManager
import io.github.seoksoon.heartoffortress.game.TeamManager
import io.github.seoksoon.heartoffortress.gui.AbilitySelectGUI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class PlayerRespawnListener(
    private val plugin: JavaPlugin,
    private val teamManager: TeamManager,
    private val spawnManager: SpawnManager,
    private val gameManager: GameManager
) : Listener {

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        val player = event.player

        if (gameManager.isRunning()) {
            // ✅ 팀별 리스폰 위치 설정
            val team = teamManager.getTeamOf(player)?.name ?: return
            val teamSpawn = spawnManager.getTeamSpawn(team)
            if (teamSpawn != null) {
                event.respawnLocation = teamSpawn
            }

            // ✅ Ability 선택 GUI는 1틱 뒤 열기 (리스폰 완료 후)
            object : BukkitRunnable() {
                override fun run() {
                    if (player.isOnline) {
                        AbilitySelectGUI.open(player)
                    }
                }
            }.runTaskLater(plugin, 1L)

            return
        }

        val defaultSpawn = spawnManager.getDefaultSpawn()
        if (defaultSpawn != null) {
            event.respawnLocation = defaultSpawn
        }
    }
}
