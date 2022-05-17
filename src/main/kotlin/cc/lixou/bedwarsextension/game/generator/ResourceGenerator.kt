package cc.lixou.bedwarsextension.game.generator

import net.kyori.adventure.text.Component
import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.ItemEntity
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.metadata.other.ArmorStandMeta
import net.minestom.server.instance.Instance
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.timer.TaskSchedule
import world.cepi.kstom.adventure.asMini
import world.cepi.kstom.util.MinestomRunnable
import java.io.Closeable
import java.time.Duration
import kotlin.math.sin

class ResourceGenerator(
    private val instance: Instance,
    point: Point,
    block: Material,
    private val resource: Material,
    name: (level: String) -> Component,
    var spawnDuration: Int
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
                    it.stand.setView(yaw, 0f)
                    if (updateNextSpawn) {
                        it.nextSpawnCount -= 1
                        if (it.nextSpawnCount == 0) {
                            it.spawnResource()
                        }
                        it.nextSpawnLine.entityMeta.customName =
                            "<yellow>Next spawn in <red><bold>${it.nextSpawnCount} <reset><yellow>seconds".asMini()
                    }
                }
            }
        }.also {
            it.schedule()
        }
    }

    private val stand = LivingEntity(EntityType.ARMOR_STAND)
    private val nameLine = Entity(EntityType.ARMOR_STAND)
    private val nextSpawnLine = Entity(EntityType.ARMOR_STAND)
    private val spawningPos = point.add(0.0, 1.0, 0.0)

    private var nextSpawnCount = spawnDuration

    init {
        stand.helmet = ItemStack.of(block)
        stand.isInvulnerable = true
        stand.isInvisible = true
        stand.setNoGravity(true)
        stand.setInstance(instance, Vec.fromPoint(point).add(0.0, 1.0, 0.0))
        var meta = stand.entityMeta as ArmorStandMeta
        meta.isMarker = true

        nameLine.isInvisible = true
        nameLine.setNoGravity(true)
        nameLine.setInstance(instance, Vec.fromPoint(point).add(0.0, 3.5, 0.0))
        meta = nameLine.entityMeta as ArmorStandMeta
        meta.isMarker = true
        meta.customName = name.invoke("I")
        meta.isCustomNameVisible = true

        nextSpawnLine.isInvisible = true
        nextSpawnLine.setNoGravity(true)
        nextSpawnLine.setInstance(instance, Vec.fromPoint(point).add(0.0, 3.0, 0.0))
        meta = nextSpawnLine.entityMeta as ArmorStandMeta
        meta.isMarker = true
        meta.customName = "<yellow>Next spawn in <red><bold>$spawnDuration <reset><yellow>seconds".asMini()
        meta.isCustomNameVisible = true

        generators.add(this)
    }

    private fun spawnResource() {
        nextSpawnCount = spawnDuration
        val item = ItemEntity(ItemStack.of(resource))
        item.setPickupDelay(Duration.ZERO)
        item.setInstance(instance, spawningPos)
    }

    override fun close() {
        generators.remove(this)
        stand.remove()
        nameLine.remove()
    }


}