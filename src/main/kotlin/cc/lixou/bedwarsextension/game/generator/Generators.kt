package cc.lixou.bedwarsextension.game.generator

import net.minestom.server.item.Material
import world.cepi.kstom.adventure.asMini

object Generators {

    val DIAMOND: GeneratorFabric = GeneratorFabric(Material.DIAMOND_BLOCK, Material.DIAMOND, {lvl -> "<gradient:dark_aqua:aqua><bold>Diamond $lvl".asMini()}, 20)

}