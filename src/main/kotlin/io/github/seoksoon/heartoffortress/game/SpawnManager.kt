package io.github.seoksoon.heartoffortress.game

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

class SpawnManager(private val plugin: JavaPlugin) {

    private val spawns: MutableMap<String, Location> = mutableMapOf()

    fun loadTeamSpawns() {
        spawns.clear()

        val section = plugin.config.getConfigurationSection("spawns")
        if (section == null) {
            plugin.logger.warning("⚠️ spawn 설정이 config.yml에 없습니다.")
            return
        }

        for (key in section.getKeys(false)) {
            val path = "spawns.$key"
            val worldName = plugin.config.getString("$path.world") ?: "world"
            val world = Bukkit.getWorld(worldName)

            if (world == null) {
                plugin.logger.warning("⚠️ 월드를 찾을 수 없습니다: $worldName")
                continue
            }

            val x = plugin.config.getDouble("$path.x")
            val y = plugin.config.getDouble("$path.y")
            val z = plugin.config.getDouble("$path.z")
            val yaw = plugin.config.getDouble("$path.yaw").toFloat()
            val pitch = plugin.config.getDouble("$path.pitch").toFloat()

            val location = Location(world, x, y, z, yaw, pitch)
            spawns[key.uppercase()] = location

            plugin.logger.info("📍 ${key.uppercase()} 리스폰 로드 완료 → $x, $y, $z")
        }

        if (spawns.isEmpty()) {
            plugin.logger.warning("⚠️ 리스폰 좌표를 하나도 불러오지 못했습니다. config.yml 확인 필요!")
        }
    }

    /** 팀 이름(RED, BLUE 등)에 해당하는 리스폰 반환 */
    fun getTeamSpawn(team: String): Location? = spawns[team.uppercase()]

    /** 기본 리스폰 (spawns.default) 반환 */
    fun getDefaultSpawn(): Location? = spawns["DEFAULT"]
}
