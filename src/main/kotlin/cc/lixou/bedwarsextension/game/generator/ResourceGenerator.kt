package cc.lixou.bedwarsextension.game.generator

import cc.lixou.bedwarsextension.game.generator.types.DroppingResourceGenerator
import cc.lixou.bedwarsextension.game.generator.types.ForgeResourceGenerator
import net.minestom.server.coordinate.Point
import net.minestom.server.instance.Instance
import net.minestom.server.timer.TaskSchedule
import world.cepi.kstom.adventure.asMini
import world.cepi.kstom.util.MinestomRunnable
import java.io.Closeable
import kotlin.math.sin

abstract class ResourceGenerator(
    protected val instance: Instance,
    protected val point: Point
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
                    if (it is DroppingResourceGenerator) {
                        it.stand.setView(yaw, 0f)
                        if (updateNextSpawn) {
                            it.nextSpawnCount -= 1
                            if (it.nextSpawnCount == 0) {
                                it.spawnResource()
                            }
                            it.nextSpawnLine.entityMeta.customName =
                                "<yellow>Next spawn in <red><bold>${it.nextSpawnCount} <reset><yellow>seconds".asMini()
                        }
                    } else if (it is ForgeResourceGenerator && updateNextSpawn) {
                        it.entries.forEach { entry ->
                            entry.nextSpawn -= 1
                            if(entry.nextSpawn == 0) {
                                it.spawnResource(entry)
                            }
                        }
                    }
                }
            }
        }.also {
            it.schedule()
        }
    }

    init {
        generators.add(this)
    }

    override fun close() {
        println("faw")
        generators.remove(this)
    }

}