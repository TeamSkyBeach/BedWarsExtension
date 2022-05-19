package cc.lixou.bedwarsextension.game.generator.fabric

import cc.lixou.bedwarsextension.game.generator.types.ForgeResourceGenerator
import net.minestom.server.item.Material
import world.cepi.kstom.adventure.asMini

object Generators {

    val DIAMOND: DroppingGeneratorFabric = DroppingGeneratorFabric(
        Material.DIAMOND_BLOCK,
        Material.DIAMOND,
        { lvl -> "<gradient:dark_aqua:aqua><bold>Diamond $lvl".asMini() },
        20
    )

    val EMERALD: DroppingGeneratorFabric = DroppingGeneratorFabric(
        Material.EMERALD_BLOCK,
        Material.EMERALD,
        { lvl -> "<gradient:dark_green:green><bold>Emerald $lvl".asMini() },
        40
    )

    val LAPIS: DroppingGeneratorFabric = DroppingGeneratorFabric(
        Material.LAPIS_BLOCK,
        Material.LAPIS_LAZULI,
        { lvl -> "<gradient:dark_blue:blue><bold>Lapis $lvl".asMini() },
        10
    )

    val BASE_FORGE: ForgeGeneratorFabric = ForgeGeneratorFabric(
        ForgeResourceGenerator.ForgeEntry(Material.IRON_INGOT, 2),
        ForgeResourceGenerator.ForgeEntry(Material.GOLD_INGOT, 5)
    )

}