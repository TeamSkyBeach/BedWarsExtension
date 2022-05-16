package cc.lixou.bedwarsextension.game.generator

import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Vec
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

class ResourceGenerator(instance: Instance, point: Point, block: Material) : Closeable {

    companion object {
        private val generators = mutableListOf<ResourceGenerator>()
        private val runnable = object : MinestomRunnable() {
            var yaw = 0f
            var i = 0.0

            override fun run() {
                yaw += sin(i).toFloat() * 21f
                i += 0.05

                generators.forEach { it.entity.setView(yaw, 0f) }
            }
        }.also {
            it.repeat(Duration.ofMillis(50))
            it.schedule()
        }
    }

    private val entity = LivingEntity(EntityType.ARMOR_STAND)

    init {
        entity.helmet = ItemStack.of(block)
        entity.isInvulnerable = true
        entity.isInvisible = true
        entity.setNoGravity(true)
        entity.setInstance(instance, Vec.fromPoint(point))
        val meta = entity.entityMeta as ArmorStandMeta
        meta.isMarker = true
        meta.customName = "<gold>Next Spawn: <red><bold>13 Seconds".asMini()
        meta.isCustomNameVisible = true

        generators.add(this)
    }

    override fun close() {
        generators.remove(this)
        entity.remove()
    }


}