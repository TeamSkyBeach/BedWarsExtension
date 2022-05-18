package cc.lixou.bedwarsextension.game.generator

import net.kyori.adventure.text.Component
import net.minestom.server.coordinate.Point
import net.minestom.server.instance.Instance
import net.minestom.server.item.Material

abstract class GeneratorFabric<T : ResourceGenerator>(
    protected val resource: Material,
    protected val spawnDuration: Int
) {
    abstract fun build(instance: Instance, point: Point): T
}

class DroppingGeneratorFabric(
    private val block: Material,
    resource: Material,
    private val name: (level: String) -> Component,
    spawnDuration: Int
) : GeneratorFabric<DroppingResourceGenerator>(resource, spawnDuration) {

    override fun build(instance: Instance, point: Point): DroppingResourceGenerator =
        DroppingResourceGenerator(instance, point, block, resource, name, spawnDuration)

}