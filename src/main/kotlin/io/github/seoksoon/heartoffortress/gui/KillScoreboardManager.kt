package io.github.seoksoon.heartoffortress.gui

import io.github.seoksoon.heartoffortress.game.TeamManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective

class KillScoreboardManager : Listener {

    private val plugin = TeamManager.getPlugin()
    private val scoreboard = TeamManager.getScoreboard()
    private val redTeam = TeamManager.getTeam("RED")!!
    private val blueTeam = TeamManager.getTeam("BLUE")!!
    private val playerKills = mutableMapOf<String, Int>()

    private val teamObjective: Objective = scoreboard.getObjective("teamKills")
        ?: scoreboard.registerNewObjective(
            "teamKills",
            "dummy",
            Component.text("Team Kills", NamedTextColor.GOLD)
        )

    private val tabObjective: Objective = scoreboard.getObjective("kills")
        ?: scoreboard.registerNewObjective(
            "kills",
            "dummy",
            Component.text("Kills", NamedTextColor.YELLOW)
        )

    init {
        teamObjective.displaySlot = DisplaySlot.SIDEBAR
        tabObjective.displaySlot = DisplaySlot.PLAYER_LIST
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler
    fun onPlayerKill(event: PlayerDeathEvent) {
        val killer = event.entity.killer ?: return
        val name = killer.name
        playerKills[name] = playerKills.getOrDefault(name, 0) + 1
        updateSidebar()
        updateTabKills()
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        playerKills.putIfAbsent(player.name, 0)
        player.scoreboard = scoreboard
        updateSidebar()
        updateTabKills()
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        updateSidebar()
        updateTabKills()
    }

    private fun updateSidebar() {
        scoreboard.entries.forEach { scoreboard.resetScores(it) }

        val redTotal = redTeam.entries.sumOf { playerKills.getOrDefault(it, 0) }
        val blueTotal = blueTeam.entries.sumOf { playerKills.getOrDefault(it, 0) }

        teamObjective.displayName(Component.text("Team Kills", NamedTextColor.GOLD))
        teamObjective.getScore("§cRED").score = redTotal
        teamObjective.getScore("§9BLUE").score = blueTotal

        Bukkit.getOnlinePlayers().forEach { it.scoreboard = scoreboard }
    }

    private fun updateTabKills() {
        Bukkit.getOnlinePlayers().forEach { player ->
            val kills = playerKills[player.name] ?: 0
            tabObjective.getScore(player.name).score = kills
            player.scoreboard = scoreboard
        }
    }

    fun reset() {
        playerKills.keys.forEach { playerKills[it] = 0 }
        updateSidebar()
        updateTabKills()
    }
}
