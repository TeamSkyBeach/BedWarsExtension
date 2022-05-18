package cc.lixou.bedwarsextension.game.generator

import net.minestom.server.coordinate.Point
import net.minestom.server.entity.ItemEntity
import net.minestom.server.instance.Instance
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.timer.TaskSchedule
import world.cepi.kstom.adventure.asMini
import world.cepi.kstom.util.MinestomRunnable
import java.io.Closeable
import java.time.Duration
import kotlin.math.sin

abstract class ResourceGenerator(
    private val instance: Instance,
    point: Point,
    private val resource: Material,
    private var spawnDuration: Int
) : Closeable {

    companion object {
        private val generators = mutableListOf<ResourceGenerator>()
        private val runnable = object : MinestomRunnable(repeat = TaskSchedule.nextTick()) {
            var yaw = 0f
            var i = 0

            override fun run() {
                yaw += sin(i.toDouble() / 20).toFloat() * 21f
                i += 1
                val updateNextSpawn = i % 20 == 0

                generators.forEach {
                    if(it is DroppingResourceGenerator) {
                        it.stand.setView(yaw, 0f)
                    }
                    if (updateNextSpawn) {
                        it.nextSpawnCount -= 1
                        if (it.nextSpawnCount == 0) {
                            it.spawnResource()
                        }
                        if(it is DroppingResourceGenerator) {
                            it.nextSpawnLine.entityMeta.customName =
                                "<yellow>Next spawn in <red><bold>${it.nextSpawnCount} <reset><yellow>seconds".asMini()
                        }
                    }
                }
            }
        }.also {
            it.schedule()
        }
    }

    private val spawningPos = point.add(0.0, 1.0, 0.0)

    private var nextSpawnCount = spawnDuration

    init {
        generators.add(this)
    }

    private fun spawnResource() {
        nextSpawnCount = spawnDuration
        val item = ItemEntity(ItemStack.of(resource))
        item.setPickupDelay(Duration.ZERO)
        item.setInstance(instance, spawningPos)
    }

    override fun close() {
        println("faw")
        generators.remove(this)
    }

}