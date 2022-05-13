package cc.lixou.bedwarsextension.inventory.shop.categories

import cc.lixou.bedwarsextension.inventory.shop.ShopCategory
import cc.lixou.stracciatella.item.CustomItem
import net.minestom.server.item.Material
import world.cepi.kstom.adventure.asMini
import world.cepi.kstom.adventure.noItalic

class BlocksShopCategory : ShopCategory(
    CustomItem("bedwars:shopcategory_blocks", Material.LIME_WOOL) {
        it.meta { meta -> meta.displayName("<gray>» <gradient:yellow:green><bold>Blöcke".asMini().noItalic()) }
    },
    Array(7) { CustomItem("bedwars:shopcategory_blocks:wool", Material.WHITE_WOOL) }
)