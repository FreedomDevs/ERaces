package dev.elysium.eraces.xpManager

import org.bukkit.Material


data class BlockXpConfig(val xpMap: Map<Material, Long>)
data class MobXpConfig(val xpMap: Map<String, Long>, val defaultXp: Long = 1L)
data class PlayerKillXpConfig(
    val minPercent: Double = 0.15,
    val maxPercent: Double = 0.35,
    val pairCooldownSeconds: Long = 300,
    val minDamagePercentToCount: Double = 0.25,
    val hourlyXpCap: Long = 2000L
)

val defaultPlayerKillXpConfig = PlayerKillXpConfig(
    minPercent = 0.15,
    maxPercent = 0.35,
    pairCooldownSeconds = 300,
    minDamagePercentToCount = 0.25,
    hourlyXpCap = 2000L
)

val defaultBlockXpConfig = BlockXpConfig(
    xpMap = mapOf(
        Material.STONE to 1L,
        Material.COBBLESTONE to 1L,
        Material.COAL_ORE to 4L,
        Material.IRON_ORE to 8L,
        Material.GOLD_ORE to 12L,
        Material.DIAMOND_ORE to 25L,
        Material.EMERALD_ORE to 30L,

        Material.NETHER_QUARTZ_ORE to 10L,
        Material.NETHER_GOLD_ORE to 15L,
        Material.ANCIENT_DEBRIS to 60L,

        Material.DEEPSLATE_COAL_ORE to 4L,
        Material.DEEPSLATE_IRON_ORE to 8L,
        Material.DEEPSLATE_GOLD_ORE to 12L,
        Material.DEEPSLATE_DIAMOND_ORE to 25L,
        Material.DEEPSLATE_EMERALD_ORE to 30L,
        Material.DEEPSLATE_COPPER_ORE to 5L,
        Material.COPPER_ORE to 5L,
        Material.DEEPSLATE_LAPIS_ORE to 12L,
        Material.LAPIS_ORE to 12L,
        Material.DEEPSLATE_REDSTONE_ORE to 15L,
        Material.REDSTONE_ORE to 15L
    )
)

val defaultMobXpConfig = MobXpConfig(
    xpMap = mapOf(
        "zombie" to 2L,
        "skeleton" to 3L,
        "creeper" to 3L,
        "spider" to 2L,
        "cave_spider" to 3L,
        "slime" to 1L,
        "magma_cube" to 5L,
        "witch" to 15L,
        "blaze" to 18L,
        "ghast" to 25L,
        "enderman" to 20L,

        "allay" to 1L,
        "axolotl" to 1L,
        "warden" to 80L,
        "froglight" to 1L,
        "frog" to 1L,
        "tadpole" to 0L,
        "sniffer" to 5L,
        "boss" to 100L
    ),
    defaultXp = 1L
)

val defaultCraftXpConfig = mapOf(
    Material.STONE_SWORD to 2L,
    Material.IRON_SWORD to 4L,
    Material.DIAMOND_SWORD to 5L,
    Material.NETHERITE_SWORD to 7L,

    Material.STONE_PICKAXE to 2L,
    Material.IRON_PICKAXE to 4L,
    Material.DIAMOND_PICKAXE to 6L,
    Material.NETHERITE_PICKAXE to 8L,

    Material.STONE_SHOVEL to 1L,
    Material.IRON_SHOVEL to 2L,
    Material.DIAMOND_SHOVEL to 4L,
    Material.NETHERITE_SHOVEL to 6L,
)