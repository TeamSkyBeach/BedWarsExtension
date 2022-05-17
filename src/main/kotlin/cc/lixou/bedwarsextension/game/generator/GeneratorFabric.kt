package cc.lixou.bedwarsextension.game.generator

import net.kyori.adventure.text.Component
import net.minestom.server.coordinate.Point
import net.minestom.server.instance.Instance
import net.minestom.server.item.Material

class GeneratorFabric(
    private val block: Material,
    private val resource: Material,
    private val name: (level: String) -> Component,
    private val spawnDuration: Int
) {

    fun build(instance: Instance, point: Point) = ResourceGenerator(instance, point, block, resource, name, spawnDuration)

}