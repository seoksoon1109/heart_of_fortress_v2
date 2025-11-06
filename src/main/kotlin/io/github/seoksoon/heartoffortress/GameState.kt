package io.github.seoksoon.heartoffortress

enum class GameState {
    /**
     * WAITING : 게임 시작 대기 중
     * COUNTDOWN : 게임이 곧 시작됨
     * RUNNING : 게임 중
     * ENDED : 게임 종료
     */
    WAITING,
    COUNTDOWN,
    RUNNING,
    ENDED
}