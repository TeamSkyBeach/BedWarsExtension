package cc.lixou.bedwarsextension.inventory

import cc.lixou.bedwarsextension.inventory.shop.ShopCategory
import cc.lixou.bedwarsextension.inventory.shop.categories.BlocksShopCategory
import cc.lixou.stracciatella.inventory.extensions.styleRadialBackground
import net.kyori.adventure.text.Component
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import world.cepi.kstom.util.setItemStack

class ShopInventory {

    var inventory: Inventory = Inventory(InventoryType.CHEST_6_ROW, Component.text("Shop"))

    init {
        redraw(BlocksShopCategory())
    }

    fun redraw(category: ShopCategory) {
        // TODO: Glasspane color per team shop npc
        inventory.styleRadialBackground(Material.GREEN_STAINED_GLASS_PANE, 4, 18 /* 2 Rows */, 9)
        inventory.setItemStack(0, category.customItem.createItemStack())
        category.insert(this)
    }

    fun setItem(x: Number, y: Number, itemStack: ItemStack) {
        inventory.setItemStack(x.toInt() + 1, y.toInt() + 3, itemStack)
    }

}