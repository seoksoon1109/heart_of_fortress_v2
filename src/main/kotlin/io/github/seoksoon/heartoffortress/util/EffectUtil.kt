package io.github.seoksoon.heartoffortress.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player

/**
 * 시각적(Title) 및 청각적(Sound) 연출 관련 유틸
 * - MessageUtil: 텍스트 기반 메시지
 * - EffectUtil: 연출 효과 관련 (Title, Sound, BossBar 등)
 */
object EffectUtil {

    /**
     * 모든 플레이어에게 Title 표시
     */
    fun showTitleToAll(
        title: String,
        subtitle: String = "",
        titleColor: NamedTextColor = NamedTextColor.WHITE,
        subtitleColor: NamedTextColor = NamedTextColor.GRAY
    ) {
        val titleComponent = Title.title(
            Component.text(title, titleColor),
            Component.text(subtitle, subtitleColor)
        )
        Bukkit.getOnlinePlayers().forEach { it.showTitle(titleComponent) }
    }

    /**
     * 단일 플레이어에게 Title 표시
     */
    fun showTitleToPlayer(
        player: Player,
        title: String,
        subtitle: String = "",
        titleColor: NamedTextColor = NamedTextColor.WHITE,
        subtitleColor: NamedTextColor = NamedTextColor.GRAY
    ) {
        val titleComponent = Title.title(
            Component.text(title, titleColor),
            Component.text(subtitle, subtitleColor)
        )
        player.showTitle(titleComponent)
    }

    /**
     * 모든 플레이어에게 사운드 재생
     */
    fun playSoundAll(sound: Sound, volume: Float = 1f, pitch: Float = 1f) {
        Bukkit.getOnlinePlayers().forEach { player ->
            player.playSound(player.location, sound, volume, pitch)
        }
    }

    /**
     * 특정 플레이어에게 사운드 재생
     */
    fun playSound(player: Player, sound: Sound, volume: Float = 1f, pitch: Float = 1f) {
        player.playSound(player.location, sound, volume, pitch)
    }
}
