package cc.lixou.bedwarsextension.game.generator.types

import cc.lixou.bedwarsextension.game.generator.ResourceGenerator
import net.minestom.server.coordinate.Point
import net.minestom.server.entity.ItemEntity
import net.minestom.server.instance.Instance
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import java.time.Duration

class ForgeResourceGenerator(
    instance: Instance,
    point: Point,
    vararg val entries: ForgeEntry
) : ResourceGenerator(instance, point) {

    fun spawnResource(entry: ForgeEntry) {
        entry.nextSpawn = entry.spawnDuration
        val item = ItemEntity(ItemStack.of(entry.resource))
        item.setPickupDelay(Duration.ZERO)
        item.setInstance(instance, point)
    }

    data class ForgeEntry(val resource: Material, val spawnDuration: Int, var nextSpawn: Int = spawnDuration)

}