package dev.elysium.eraces.items.колчаны

import dev.elysium.eraces.items.core.Item
import dev.elysium.eraces.items.core.ItemType
import dev.elysium.eraces.utils.ChatUtil
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import io.papermc.paper.datacomponent.item.ItemLore
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
class Колчаны(override val id: String, val name: String, val lore: ItemLore) : Item {
    final override val type = ItemType.КОЛЧАНЫ

    override fun onInit(item: ItemStack) {
        item.setData(DataComponentTypes.CUSTOM_NAME, ChatUtil.parse(name));

        item.setData(DataComponentTypes.LORE, lore)

        item.setData(
            DataComponentTypes.CUSTOM_MODEL_DATA,
            CustomModelData.customModelData().addString(id).build()
        );
    }
}