package io.github.seoksoon.heartoffortress.command

import io.github.seoksoon.heartoffortress.HeartOfFortressPlugin
import io.github.seoksoon.heartoffortress.game.GameManager

object CommandRegistry {
    fun registerAll(plugin: HeartOfFortressPlugin, gameManager: GameManager) {
        plugin.getCommand("hof")?.setExecutor(HofCommand(gameManager))
            ?: plugin.logger.severe("⚠️ /hof 명령어 등록 실패 — plugin.yml을 확인하세요.")

        plugin.logger.info("✅ 명령어 등록 완료: hof")
    }
}


