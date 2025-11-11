package io.github.seoksoon.heartoffortress.ability

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

enum class AbilityType(
    val displayName: String,
    val icon: Material,
    val color: NamedTextColor,
    val lore: List<String>
) {
    WARRIOR(
        "전사",
        Material.NETHERITE_AXE,
        NamedTextColor.RED,
        listOf(
            "근접 공격 특화 전사.",
            "높은 체력과 피해량을 자랑하며, 3티어부터 방패를 사용합니다."
        )
    ) {
        override fun getWeapon(tier: AbilityTier): ItemStack {
            val material = when (tier) {
                AbilityTier.TIER1 -> Material.WOODEN_AXE
                AbilityTier.TIER2 -> Material.STONE_AXE
                AbilityTier.TIER3 -> Material.IRON_AXE
                AbilityTier.TIER4 -> Material.DIAMOND_AXE
            }
            return createWeapon(material, this, tier)
        }

        override fun getArmorSet(tier: AbilityTier): List<ItemStack> {
            val armor = getArmorByTier(tier)
            val items = mutableListOf<ItemStack>()
            // 전사: 헬멧 + 갑옷만 지급
            items.addAll(listOf(armor.helmet, armor.chestplate))
            // 3티어 이상은 방패 추가
            if (tier.level >= 3) items.add(ItemStack(Material.SHIELD))
            return items
        }
    },

    ARCHER(
        "궁수",
        Material.BOW,
        NamedTextColor.GREEN,
        listOf(
            "원거리 공격 전문.",
            "적을 멀리서 제압하며, 가벼운 갑옷만 착용합니다."
        )
    ) {
        override fun getWeapon(tier: AbilityTier): ItemStack {
            return createWeapon(Material.BOW, this, tier)
        }

        override fun getArmorSet(tier: AbilityTier): List<ItemStack> {
            val armor = getArmorByTier(tier)
            // 궁수: 흉갑만 지급
            return listOf(armor.chestplate)
        }
    },

    ASSASSIN(
        "암살자",
        Material.ENDER_PEARL,
        NamedTextColor.DARK_PURPLE,
        listOf(
            "은신과 기습에 특화된 암살자.",
            "방어구를 착용하지 않고 빠르게 움직입니다."
        )
    ) {
        override fun getWeapon(tier: AbilityTier): ItemStack {
            val material = when (tier) {
                AbilityTier.TIER1 -> Material.WOODEN_SWORD
                AbilityTier.TIER2 -> Material.STONE_SWORD
                AbilityTier.TIER3 -> Material.IRON_SWORD
                AbilityTier.TIER4 -> Material.DIAMOND_SWORD
            }
            return createWeapon(material, this, tier)
        }

        override fun getArmorSet(tier: AbilityTier): List<ItemStack> {
            // 암살자는 방어구 없음
            return emptyList()
        }
    },

    MINER(
        "광부",
        Material.IRON_PICKAXE,
        NamedTextColor.YELLOW,
        listOf(
            "방어 및 보조 역할의 전문가.",
            "가장 튼튼한 방어구를 착용합니다."
        )
    ) {
        override fun getWeapon(tier: AbilityTier): ItemStack {
            val material = when (tier) {
                AbilityTier.TIER1 -> Material.WOODEN_PICKAXE
                AbilityTier.TIER2 -> Material.STONE_PICKAXE
                AbilityTier.TIER3 -> Material.IRON_PICKAXE
                AbilityTier.TIER4 -> Material.DIAMOND_PICKAXE
            }
            return createWeapon(material, this, tier)
        }

        override fun getArmorSet(tier: AbilityTier): List<ItemStack> {
            val armor = getArmorByTier(tier)
            // 광부: 풀세트 지급
            return listOf(armor.helmet, armor.chestplate, armor.leggings, armor.boots)
        }
    };

    abstract fun getWeapon(tier: AbilityTier): ItemStack
    abstract fun getArmorSet(tier: AbilityTier): List<ItemStack>

    companion object {
        fun fromIcon(icon: Material): AbilityType? = entries.find { it.icon == icon }

        /** 공통 무기 생성 로직 */
        fun createWeapon(material: Material, ability: AbilityType, tier: AbilityTier): ItemStack {
            val weapon = ItemStack(material)
            val meta = weapon.itemMeta
            meta.displayName(
                Component.text("${ability.displayName} 능력 Lv.${tier.level}")
                    .color(ability.color)
            )
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
            weapon.itemMeta = meta
            return weapon
        }

        /** 티어에 따른 방어구 재질 매핑 */
        private fun getArmorByTier(tier: AbilityTier): ArmorSet {
            val prefix = when (tier) {
                AbilityTier.TIER1 -> "LEATHER"
                AbilityTier.TIER2 -> "CHAINMAIL"
                AbilityTier.TIER3 -> "IRON"
                AbilityTier.TIER4 -> "DIAMOND"
            }

            fun mat(name: String): Material = Material.valueOf("${prefix}_${name}")

            return ArmorSet(
                helmet = ItemStack(mat("HELMET")),
                chestplate = ItemStack(mat("CHESTPLATE")),
                leggings = ItemStack(mat("LEGGINGS")),
                boots = ItemStack(mat("BOOTS"))
            )
        }
    }
}

/** 능력 티어 (전직 단계) */
enum class AbilityTier(val level: Int) {
    TIER1(1),
    TIER2(2),
    TIER3(3),
    TIER4(4)
}

/** 방어구 세트 데이터 클래스 */
data class ArmorSet(
    val helmet: ItemStack,
    val chestplate: ItemStack,
    val leggings: ItemStack,
    val boots: ItemStack
)
