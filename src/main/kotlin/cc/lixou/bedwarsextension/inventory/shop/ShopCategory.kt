package cc.lixou.bedwarsextension.inventory.shop

import cc.lixou.bedwarsextension.inventory.ShopInventory
import cc.lixou.stracciatella.item.CustomItem

abstract class ShopCategory(
    val customItem: CustomItem,
    val itemMap: Array<CustomItem>
) {

    fun insert(shopInventory: ShopInventory) {
        itemMap.forEachIndexed { index, customItem ->
            val isEven = index % 2
            shopInventory.setItem(index, isEven, customItem.createItemStack())
        }
    }

}