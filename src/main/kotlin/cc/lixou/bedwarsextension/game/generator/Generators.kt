package cc.lixou.bedwarsextension.game.generator

import net.minestom.server.item.Material
import world.cepi.kstom.adventure.asMini

object Generators {

    val DIAMOND: DroppingGeneratorFabric = DroppingGeneratorFabric(Material.DIAMOND_BLOCK, Material.DIAMOND, {lvl -> "<gradient:dark_aqua:aqua><bold>Diamond $lvl".asMini()}, 20)

    val EMERALD: DroppingGeneratorFabric = DroppingGeneratorFabric(Material.EMERALD_BLOCK, Material.EMERALD, {lvl -> "<gradient:dark_green:green><bold>Emerald $lvl".asMini()}, 40)

    val LAPIS: DroppingGeneratorFabric = DroppingGeneratorFabric(Material.LAPIS_BLOCK, Material.LAPIS_LAZULI, {lvl -> "<gradient:dark_blue:blue><bold>Lapis $lvl".asMini()}, 10)

}