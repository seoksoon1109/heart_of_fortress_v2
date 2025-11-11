package io.github.seoksoon.heartoffortress.game

import io.github.seoksoon.heartoffortress.GameState
import io.github.seoksoon.heartoffortress.ability.AbilityManager
import io.github.seoksoon.heartoffortress.util.EffectUtil
import io.github.seoksoon.heartoffortress.util.MessageUtil
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class GameManager(private val plugin: JavaPlugin) {

    var state: GameState = GameState.WAITING
        private set
    private var countdownTask: BukkitRunnable? = null

    /**
     * 게임 시작 카운트다운 진입
     */
    fun startCountdown() {
        if (isRunning() || isCountdown()) {
            MessageUtil.broadcast("§c이미 게임이 시작 중입니다!")
            return
        }

        state = GameState.COUNTDOWN
        MessageUtil.debug("GameState 변경: WAITING → COUNTDOWN")

        var timeLeft = 5
        EffectUtil.showTitleToAll("§e게임이 ${timeLeft}초 후 시작됩니다!", "준비하세요!", NamedTextColor.GOLD)

        countdownTask = object : BukkitRunnable() {
            override fun run() {
                if (timeLeft <= 0) {
                    EffectUtil.showTitleToAll("§a게임 시작!", "", NamedTextColor.GREEN)
                    EffectUtil.playSoundAll(Sound.UI_TOAST_CHALLENGE_COMPLETE)
                    startGame()
                    cancel()
                    return
                }

                // 남은 시간 Title 표시
                EffectUtil.showTitleToAll(
                    "$timeLeft",
                    "",
                    NamedTextColor.GOLD
                )

                // 경험치 먹는 소리로 카운트다운 효과
                EffectUtil.playSoundAll(Sound.ENTITY_EXPERIENCE_ORB_PICKUP)

                timeLeft--
            }
        }

        countdownTask!!.runTaskTimer(plugin, 0L, 20L)
    }

    /**
     * 카운트 다운 중단
     */

    fun cancelCountdown() {
        if (!isCountdown()) return
        countdownTask?.cancel()
        countdownTask = null
        state = GameState.WAITING
    }

    /**
     * 실제 게임 시작
     */
    fun startGame() {
        if (!isCountdown()) {
            MessageUtil.broadcast("§c카운트다운 상태에서만 시작할 수 있습니다!")
            return
        }
        initGame()
        setSpectator()
        state = GameState.RUNNING
        MessageUtil.broadcast("§a게임이 시작되었습니다!")
        MessageUtil.debug("GameState 변경: COUNTDOWN → RUNNING")
        for (player in Bukkit.getOnlinePlayers()) {
            if (player.isDead) continue
            player.health = 0.0
        }
    }

    /**
     * 게임 종료 처리
     */
    fun stopGame(force: Boolean = false) {
        if (isWaiting()) {
            MessageUtil.broadcast("&c아직 시작되지 않은 게임은 중단할 수 없습니다.")
            return
        }

        if (isCountdown()) {
            MessageUtil.broadcast("&c관리자에 의해 시작 카운트 다운이 중단되었습니다!")
            cancelCountdown()
            return
        }

        if (force) {
            MessageUtil.broadcast("&c관리자에 의해 게임이 강제 종료되었습니다.")
            resetGame()
            return
        }

        // 일반적인 게임 종료 시
        state = GameState.ENDED
        MessageUtil.broadcast("&e게임이 종료되었습니다!")
        EffectUtil.playSoundAll(Sound.UI_TOAST_CHALLENGE_COMPLETE)

        // 결과 표시 후 잠시 뒤 리셋
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            resetGame()
        }, 100L) // 약 5초 후
    }

    fun initGame(){
        AbilityManager.resetAllLevels()
        MessageUtil.debug("initGame() 호출 완료")
    }


    /**
     * 게임 초기화
     */
    fun resetGame() {
        state = GameState.WAITING
        MessageUtil.broadcast("§7게임이 초기화되었습니다. 플레이어를 대기 상태로 복귀시킵니다.")
        MessageUtil.log("GameState 변경: ENDED → WAITING")
        resetGameMode()
        // TODO: 월드 리셋 / 하트 복원 등 초기화 로직
    }

    /**
     * 팀이 존재하지 않는 사람 관전자로 전환
     */

    private fun setSpectator() {
        for (player in Bukkit.getOnlinePlayers()) {
            if (!TeamManager.isInAnyTeam(player)) {
                player.gameMode = GameMode.SPECTATOR
                MessageUtil.send(player, "팀에 속하지 않아 관전 모드로 전환되었습니다.", NamedTextColor.GRAY)
            } else {
                player.gameMode = GameMode.SURVIVAL
            }
        }
    }

    private fun resetGameMode() {
        for (player in Bukkit.getOnlinePlayers()) {
            player.gameMode = GameMode.CREATIVE
        }
    }
    fun isRunning(): Boolean = state == GameState.RUNNING
    fun isWaiting(): Boolean = state == GameState.WAITING
    fun isCountdown(): Boolean = state == GameState.COUNTDOWN
    fun isEnded(): Boolean = state == GameState.ENDED
}
