package cc.lixou.bedwarsextension.game.generator

import net.kyori.adventure.text.Component
import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.metadata.other.ArmorStandMeta
import net.minestom.server.instance.Instance
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import world.cepi.kstom.adventure.asMini
import world.cepi.kstom.util.MinestomRunnable
import java.io.Closeable
import java.time.Duration
import kotlin.math.sin

class ResourceGenerator(instance: Instance, point: Point, block: Material, name: Component) : Closeable {

    companion object {
        private val generators = mutableListOf<ResourceGenerator>()
        private val runnable = object : MinestomRunnable() {
            var yaw = 0f
            var i = 0.0

            override fun run() {
                yaw += sin(i).toFloat() * 21f
                i += 0.05

                generators.forEach { it.stand.setView(yaw, 0f) }
            }
        }.also {
            it.repeat(Duration.ofMillis(50))
            it.schedule()
        }
    }

    private val stand = LivingEntity(EntityType.ARMOR_STAND)
    private val nameLine = Entity(EntityType.ARMOR_STAND)

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
        meta.customName = name
        meta.isCustomNameVisible = true

        generators.add(this)
    }

    override fun close() {
        generators.remove(this)
        stand.remove()
        nameLine.remove()
    }


}