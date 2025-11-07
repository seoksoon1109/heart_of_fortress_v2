package io.github.seoksoon.heartoffortress.game

import io.github.seoksoon.heartoffortress.GameState
import io.github.seoksoon.heartoffortress.util.MessageUtil
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
        if (state != GameState.WAITING) {
            MessageUtil.broadcast("§c이미 게임이 시작 중입니다!")
            return
        }

        state = GameState.COUNTDOWN
        MessageUtil.broadcast("§e게임이 10초 후 시작됩니다!")

        // TODO: CountDown 로직 구현 예정 (TaskScheduler)
        MessageUtil.debug("GameState 변경: WAITING → COUNTDOWN")
        var timeLeft = 10

        countdownTask = object : BukkitRunnable(){
            override fun run() {
                if (timeLeft <= 0) {
                    cancel()
                    startGame()
                    return
                } else {
                    MessageUtil.broadcast("${timeLeft}초 후 게임이 시작됩니다!")
                }
                timeLeft -= 1
            }
        }
        countdownTask!!.runTaskTimer(plugin, 0L,20L)
    }

    /**
     * 카운트 다운 중단
     */

    fun cancelCountdown() {
        countdownTask?.cancel()
        countdownTask = null
        state = GameState.WAITING
        MessageUtil.broadcast("§c카운트다운이 취소되었습니다.")
    }

    /**
     * 실제 게임 시작
     */
    fun startGame() {
        if (state != GameState.COUNTDOWN) {
            MessageUtil.broadcast("§c카운트다운 상태에서만 시작할 수 있습니다!")
            return
        }

        state = GameState.RUNNING
        MessageUtil.broadcast("§a게임이 시작되었습니다!")
        MessageUtil.debug("GameState 변경: COUNTDOWN → RUNNING")

        // TODO: 초기화 로직 (팀 배정, 하트 체력 설정 등)
    }

    /**
     * 게임 종료 처리
     */
    fun endGame() {
        if (state != GameState.RUNNING) {
            MessageUtil.broadcast("§c진행 중인 게임이 없습니다.")
            return
        }

        state = GameState.ENDED
        MessageUtil.broadcast("§c게임이 종료되었습니다!")
        MessageUtil.debug("GameState 변경: RUNNING → ENDED")

        // TODO: 승리 조건 판정 로직 추가
    }

    /**
     * 게임 초기화
     */
    fun resetGame() {
        state = GameState.WAITING
        MessageUtil.broadcast("§7게임이 초기화되었습니다. 플레이어를 대기 상태로 복귀시킵니다.")
        MessageUtil.log("GameState 변경: ENDED → WAITING")

        // TODO: 월드 리셋 / 하트 복원 등 초기화 로직
    }
}
