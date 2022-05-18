package cc.lixou.bedwarsextension.game.generator.fabric

import cc.lixou.bedwarsextension.game.generator.types.DroppingResourceGenerator
import cc.lixou.bedwarsextension.game.generator.ResourceGenerator
import cc.lixou.bedwarsextension.game.generator.types.ForgeResourceGenerator
import net.kyori.adventure.text.Component
import net.minestom.server.coordinate.Point
import net.minestom.server.instance.Instance
import net.minestom.server.item.Material

abstract class GeneratorFabric<T : ResourceGenerator>() {
    abstract fun build(instance: Instance, point: Point): T
}

class DroppingGeneratorFabric(
    private val block: Material,
    private val resource: Material,
    private val name: (level: String) -> Component,
    private val spawnDuration: Int
) : GeneratorFabric<DroppingResourceGenerator>() {

    override fun build(instance: Instance, point: Point): DroppingResourceGenerator =
        DroppingResourceGenerator(instance, point, block, resource, name, spawnDuration)

}

class ForgeGeneratorFabric(
    private vararg val entries: ForgeResourceGenerator.ForgeEntry
) : GeneratorFabric<ForgeResourceGenerator>() {

    override fun build(instance: Instance, point: Point): ForgeResourceGenerator =
        ForgeResourceGenerator(instance, point, *entries)

}