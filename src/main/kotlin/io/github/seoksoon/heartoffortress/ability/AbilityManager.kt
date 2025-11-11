package io.github.seoksoon.heartoffortress.ability

import io.github.seoksoon.heartoffortress.HeartOfFortressPlugin
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

object AbilityManager {
    private val plugin = HeartOfFortressPlugin.instance
    private val abilityKey = NamespacedKey(plugin, "ability")
    private val levelKey = NamespacedKey(plugin, "ability_level")

    fun setAbility(player: Player, ability: AbilityType) {
        val data = player.persistentDataContainer
        data.set(abilityKey, PersistentDataType.STRING, ability.name)
        data.set(levelKey, PersistentDataType.INTEGER, 1)
    }

    fun getAbility(player: Player): AbilityType? {
        val name = player.persistentDataContainer.get(abilityKey, PersistentDataType.STRING) ?: return null
        return runCatching { AbilityType.valueOf(name) }.getOrNull()
    }

    fun getTier(player: Player): AbilityTier {
        val level = player.persistentDataContainer.get(levelKey, PersistentDataType.INTEGER) ?: 1
        return AbilityTier.entries.firstOrNull { it.level == level } ?: AbilityTier.TIER1
    }

    fun setLevel(player: Player, level: Int) {
        val data = player.persistentDataContainer
        data.set(levelKey, PersistentDataType.INTEGER, level.coerceIn(1, 4))
    }

    fun resetAllLevels() {
        Bukkit.getOnlinePlayers().forEach { player ->
            val data = player.persistentDataContainer
            data.set(levelKey, PersistentDataType.INTEGER, 1)
        }
    }

    fun giveEquipment(player: Player) {
        val ability = getAbility(player) ?: return
        val tier = getTier(player)

        player.inventory.clear()

        val weapon = ability.getWeapon(tier)
        player.inventory.setItemInMainHand(weapon) // 오른손(주무기)

        val armorSet = ability.getArmorSet(tier)

        armorSet.forEach { armor ->
            when (armor.type) {
                org.bukkit.Material.LEATHER_HELMET,
                org.bukkit.Material.CHAINMAIL_HELMET,
                org.bukkit.Material.IRON_HELMET,
                org.bukkit.Material.DIAMOND_HELMET ->
                    player.inventory.helmet = armor

                org.bukkit.Material.LEATHER_CHESTPLATE,
                org.bukkit.Material.CHAINMAIL_CHESTPLATE,
                org.bukkit.Material.IRON_CHESTPLATE,
                org.bukkit.Material.DIAMOND_CHESTPLATE ->
                    player.inventory.chestplate = armor

                org.bukkit.Material.LEATHER_LEGGINGS,
                org.bukkit.Material.CHAINMAIL_LEGGINGS,
                org.bukkit.Material.IRON_LEGGINGS,
                org.bukkit.Material.DIAMOND_LEGGINGS ->
                    player.inventory.leggings = armor

                org.bukkit.Material.LEATHER_BOOTS,
                org.bukkit.Material.CHAINMAIL_BOOTS,
                org.bukkit.Material.IRON_BOOTS,
                org.bukkit.Material.DIAMOND_BOOTS ->
                    player.inventory.boots = armor

                org.bukkit.Material.SHIELD ->
                    player.inventory.setItemInOffHand(armor)

                else -> player.inventory.addItem(armor)
            }
        }
        player.updateInventory()
    }

}
