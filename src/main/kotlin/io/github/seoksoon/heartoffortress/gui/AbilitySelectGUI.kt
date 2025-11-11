package io.github.seoksoon.heartoffortress.gui

import io.github.seoksoon.heartoffortress.ability.AbilityManager
import io.github.seoksoon.heartoffortress.ability.AbilityType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object AbilitySelectGUI : Listener {

    private const val TITLE = "✨ 능력 선택"

    fun open(player: Player) {
        val inv: Inventory = Bukkit.createInventory(null, 27, Component.text(TITLE, NamedTextColor.GOLD))
        val filler = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
        val fillerMeta = filler.itemMeta
        fillerMeta.displayName(Component.text(" "))
        filler.itemMeta = fillerMeta
        for (i in 0 until inv.size) inv.setItem(i, filler)

        val abilitySlots = listOf(10, 12, 14, 16)

        AbilityType.entries.forEachIndexed { index, ability ->
            if (index >= abilitySlots.size) return@forEachIndexed
            val slot = abilitySlots[index]
            val tier = AbilityManager.getTier(player)
            val weaponIcon = ability.getWeapon(tier)
            val meta = weaponIcon.itemMeta
            meta.displayName(Component.text("${ability.displayName} Lv.${tier.level}", ability.color))
            meta.lore(ability.lore.map { Component.text(it, NamedTextColor.GRAY) })
            weaponIcon.itemMeta = meta
            inv.setItem(slot, weaponIcon)
        }

        player.openInventory(inv)
    }

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        if (event.view.title() != Component.text(TITLE, NamedTextColor.GOLD)) return
        event.isCancelled = true

        val item = event.currentItem ?: return
        val displayComponent = item.itemMeta.displayName() ?: return

        val displayName = PlainTextComponentSerializer.plainText().serialize(displayComponent)

        val ability = AbilityType.entries.firstOrNull {
            displayName.contains(it.displayName)
        } ?: return

        player.closeInventory()
        AbilityManager.setAbility(player, ability)
        AbilityManager.giveEquipment(player)
        player.sendMessage("§a당신은 §e${ability.displayName}§a 능력을 선택했습니다!")
    }


    @EventHandler
    fun onDrag(event: InventoryDragEvent) {
        if (event.view.title() == Component.text(TITLE, NamedTextColor.GOLD)) event.isCancelled = true
    }
}
