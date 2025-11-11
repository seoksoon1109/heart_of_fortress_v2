package io.github.seoksoon.heartoffortress.game

import io.github.seoksoon.heartoffortress.util.MessageUtil
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team
import java.time.Duration

object TeamManager {
    private lateinit var plugin: JavaPlugin
    private lateinit var scoreboard: Scoreboard

    fun init(plugin: JavaPlugin) {
        this.plugin = plugin
        this.scoreboard = Bukkit.getScoreboardManager().mainScoreboard

        setupTeam("RED", NamedTextColor.RED, Component.text("", NamedTextColor.RED))
        setupTeam("BLUE", NamedTextColor.BLUE, Component.text("", NamedTextColor.BLUE))

        MessageUtil.log("✅ TeamManager initialized.")
    }

    fun getPlugin(): JavaPlugin = plugin

    private fun setupTeam(name: String, color: NamedTextColor, prefix: Component) {
        val team = scoreboard.getTeam(name) ?: scoreboard.registerNewTeam(name)
        team.color(color)
        team.prefix(prefix)
        team.setAllowFriendlyFire(false)
        team.setCanSeeFriendlyInvisibles(true)
    }

    fun getScoreboard(): Scoreboard = scoreboard
    fun getTeam(name: String): Team? = scoreboard.getTeam(name.uppercase())

    fun joinTeam(player: Player, teamName: String): Boolean {
        val normalized = teamName.uppercase()
        val team = getTeam(normalized)
            ?: return MessageUtil.send(player, "&c존재하지 않는 팀입니다. (red / blue)").let { false }

        // 기존 팀에서 제거
        scoreboard.teams.forEach { it.removeEntry(player.name) }

        team.addEntry(player.name)
        player.scoreboard = scoreboard

        MessageUtil.send(player, "&a$normalized 팀에 참가했습니다!")
        MessageUtil.log("${player.name} joined $normalized team.")
        return true
    }

    /** 🎲 모든 플레이어 랜덤 팀 배정 (룰렛 효과 포함) */
    fun joinRandomAll() {
        val players = Bukkit.getOnlinePlayers().toList()
        if (players.isEmpty()) {
            MessageUtil.log("No players online for random team assignment.")
            return
        }

        val red = getTeam("RED")!!
        val blue = getTeam("BLUE")!!

        var rollCount = 0
        val totalRolls = 50
        val startDelay = 5L
        val maxDelay = 2L

        fun rollNext() {
            if (rollCount >= totalRolls) {
                players.forEach { player -> assignFinalTeam(player, red, blue) }
                return
            }

            rollCount++
            val progress = (rollCount.toDouble() / totalRolls).coerceIn(0.05, 0.95)
            val adjustedProgress = 0.05 + 0.9 * progress
            val sinFactor = kotlin.math.sin(adjustedProgress * Math.PI)
            val currentDelay = (startDelay - (startDelay - maxDelay) * sinFactor).toLong()

            val isRed = rollCount % 2 == 0
            val teamName = if (isRed) "RED" else "BLUE"
            val color = if (isRed) NamedTextColor.RED else NamedTextColor.BLUE
            val pitch = 1.0f + ((1f - sinFactor.toFloat()) * 0.3f)

            players.forEach { player ->
                player.showTitle(
                    Title.title(
                        Component.text(teamName, color),
                        Component.text("", NamedTextColor.GRAY),
                        Title.Times.times(Duration.ZERO, Duration.ofMillis(150), Duration.ZERO)
                    )
                )
                player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, pitch)
            }

            Bukkit.getScheduler().runTaskLater(plugin, Runnable { rollNext() }, currentDelay)
        }

        MessageUtil.broadcast("&e모든 플레이어가 랜덤으로 팀이 배정됩니다...")
        rollNext()
    }

    private fun assignFinalTeam(player: Player, red: Team, blue: Team) {
        val target = if (red.entries.size <= blue.entries.size) red else blue
        val teamName = target.name
        val color = target.color()

        scoreboard.teams.forEach { it.removeEntry(player.name) }
        target.addEntry(player.name)
        player.scoreboard = scoreboard

        player.showTitle(
            Title.title(
                Component.text("$teamName TEAM!", color),
                Component.text("배정 완료!", NamedTextColor.GRAY),
                Title.Times.times(Duration.ofMillis(300), Duration.ofMillis(1200), Duration.ofMillis(400))
            )
        )
        player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)

        val message = Component.text()
            .append(Component.text("[HOF] ", NamedTextColor.GOLD))
            .append(Component.text("당신은 ", NamedTextColor.GREEN))
            .append(Component.text(teamName, color))
            .append(Component.text(" 팀으로 배정되었습니다!", NamedTextColor.GREEN))
            .build()
        player.sendMessage(message)

        MessageUtil.debug("${player.name} assigned to $teamName team.")
    }

    fun leaveTeam(player: Player) {
        val current = getTeamOf(player)
        if (current != null) {
            current.removeEntry(player.name)
            MessageUtil.send(player, "&7팀에서 나왔습니다.")
        } else {
            MessageUtil.send(player, "&c현재 소속된 팀이 없습니다.")
        }
    }

    fun listTeams(sender: Player) {
        val red = getTeam("RED")!!
        val blue = getTeam("BLUE")!!

        MessageUtil.send(sender, "&6=== 팀 현황 ===")
        MessageUtil.send(sender, "&cRED 팀 (${red.entries.size}명)")
        MessageUtil.send(sender, "&9BLUE 팀 (${blue.entries.size}명)")
    }

    fun resetTeams(sender: Player) {
        scoreboard.teams.forEach { it.entries.toList().forEach(it::removeEntry) }
        MessageUtil.send(sender, "&c모든 팀이 초기화되었습니다.")
        MessageUtil.log("All teams reset by ${sender.name}.")
    }

    fun isInAnyTeam(player: Player): Boolean = getTeamOf(player) != null
    fun getTeamOf(player: Player): Team? = scoreboard.getEntryTeam(player.name)
}
