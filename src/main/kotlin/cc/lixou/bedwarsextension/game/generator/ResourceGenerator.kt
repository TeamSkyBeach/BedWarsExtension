package cc.lixou.bedwarsextension.game.generator

import cc.lixou.bedwarsextension.game.generator.types.DroppingResourceGenerator
import cc.lixou.bedwarsextension.game.generator.types.ForgeResourceGenerator
import net.minestom.server.coordinate.Point
import net.minestom.server.entity.ItemEntity
import net.minestom.server.instance.Instance
import net.minestom.server.tag.Tag
import net.minestom.server.timer.TaskSchedule
import world.cepi.kstom.adventure.asMini
import world.cepi.kstom.util.MinestomRunnable
import java.io.Closeable
import java.util.UUID
import kotlin.math.sin

abstract class ResourceGenerator(
    protected val instance: Instance,
    protected val point: Point
) : Closeable {

    companion object {
        val uuidTag = Tag.UUID("genID")

        private val generators = mutableMapOf<UUID, ResourceGenerator>()
        private val runnable = object : MinestomRunnable(repeat = TaskSchedule.nextTick()) {
            var yaw = 0f
            var i = 0

            override fun run() {
                yaw += sin(i.toDouble() / 20).toFloat() * 21f
                i += 1
                val updateNextSpawn = i % 20 == 0

                generators.values.forEach {
                    if (it is DroppingResourceGenerator) {
                        it.stand.setView(yaw, 0f)
                        if (updateNextSpawn) {
                            it.nextSpawnCount -= 1
                            if (it.nextSpawnCount == 0) {
                                it.resetCountdown()
                                if(it.canSpawn()) it.spawnResource()
                            }
                            it.nextSpawnLine.entityMeta.customName =
                                "<yellow>Next spawn in <red><bold>${it.nextSpawnCount} <reset><yellow>seconds".asMini()
                        }
                    } else if (it is ForgeResourceGenerator && updateNextSpawn) {
                        it.entries.forEach { entry ->
                            entry.nextSpawn -= 1
                            if(entry.nextSpawn == 0) {
                                it.resetCountdown(entry)
                                if(it.canSpawn()) it.spawnResource(entry)
                            }
                        }
                    }
                }
            }
        }.also {
            it.schedule()
        }

        fun mayPickup(item: ItemEntity) {
            val tag = item.getTag(uuidTag)
            val generator = generators[tag] ?: return
            generator.mayPickup(item)
        }
    }

    val uuid = UUID.randomUUID()

    init {
        generators[uuid] = this
    }

    override fun close() {
        println("faw")
        generators.keys.forEach { id -> if(id == uuid) generators.remove(id) }
    }

    abstract fun mayPickup(item: ItemEntity)

}