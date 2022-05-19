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
    private val maxItems: Int,
    vararg val entries: ForgeEntry
) : ResourceGenerator(instance, point) {

    private var currentItems = 0

    fun canSpawn(): Boolean = currentItems < maxItems

    fun resetCountdown(entry: ForgeEntry) {
        entry.nextSpawn = entry.spawnDuration
    }

    fun spawnResource(entry: ForgeEntry) {
        val item = ItemEntity(ItemStack.of(entry.resource))
        item.isMergeable = false
        item.setTag(uuidTag, uuid)
        item.setPickupDelay(Duration.ZERO)
        currentItems++
        item.setInstance(instance, point)
    }

    data class ForgeEntry(val resource: Material, val spawnDuration: Int, var nextSpawn: Int = spawnDuration)

    override fun mayPickup(item: ItemEntity) {
        if(!entries.any { it.resource == item.itemStack.material() }) return
        currentItems -= 1
    }

}