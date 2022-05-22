package cc.lixou.bedwarsextension.game

import cc.lixou.bedwarsextension.game.generator.ResourceGenerator
import cc.lixou.bedwarsextension.game.generator.fabric.Generators
import cc.lixou.bedwarsextension.game.team.getBedWarsTeam
import cc.lixou.bedwarsextension.game.team.BedWarsTeam
import cc.lixou.bedwarsextension.game.team.TeamTypes
import cc.lixou.bedwarsextension.inventory.ShopInventory
import cc.lixou.stracciatella.game.Game
import net.minestom.server.entity.ItemEntity
import net.minestom.server.entity.Player
import net.minestom.server.event.entity.EntityAttackEvent
import net.minestom.server.event.item.ItemDropEvent
import net.minestom.server.event.item.PickupItemEvent
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.network.packet.server.play.EntityAnimationPacket
import net.minestom.server.utils.time.TimeUnit
import world.cepi.kstom.Manager
import world.cepi.kstom.event.listenOnly
import kotlin.math.cos
import kotlin.math.sin

class BedWarsGame : Game() {

    var currentState: BedWarsGameState = BedWarsGameState.WAITING

    private val TEAM_AMOUNT = 8
    private val PLAYER_PER_TEAM = 2

    val teams = mutableListOf<BedWarsTeam>()

    init {
        instance = Manager.instance.instances.first()
        // DEBUG
        eventNode.listenOnly<PlayerChatEvent> {
            if (message.lowercase() == "play") {
                currentState = BedWarsGameState.INGAME
            } else if (message.lowercase() == "shop") {
                player.openInventory(ShopInventory())
            } else if (message.lowercase() == "generator") {
                Generators.DIAMOND.build(player.instance!!, player.position, 1)
            }
        }
        // ENDDEBUG
        eventNode.listenOnly<ItemDropEvent> {
            when (currentState) {
                BedWarsGameState.INGAME -> {
                    val itemEntity = ItemEntity(itemStack)
                    itemEntity.setPickupDelay(40, TimeUnit.SERVER_TICK)
                    itemEntity.setInstance(instance, player.position.add(0.0, 1.5, 0.0))
                    itemEntity.isGlowing = true // TODO: Glowing in team color
                    itemEntity.velocity = player.position.direction().mul(6.0)
                }
                else -> isCancelled = true
            }
        }
        eventNode.listenOnly<PickupItemEvent> {
            when (currentState) {
                BedWarsGameState.INGAME -> {
                    if (entity is Player) {
                        ResourceGenerator.mayPickup(itemEntity)
                        (entity as Player).inventory.addItemStack(itemStack)
                    } else {
                        isCancelled = true
                    }
                }
                else -> isCancelled = true
            }
        }
        eventNode.listenOnly<EntityAttackEvent> {
            if (currentState != BedWarsGameState.INGAME) return@listenOnly
            val attacker = entity
            if (attacker !is Player) return@listenOnly
            if (attacker.getBedWarsTeam()!!.players.contains(target as Player)) return@listenOnly
            target.takeKnockback(
                0.4f,
                sin(attacker.position.yaw * (Math.PI / 180)),
                -cos(attacker.position.yaw * (Math.PI / 180))
            )
            (target as Player).sendPacketToViewersAndSelf(
                EntityAnimationPacket(
                    target.entityId,
                    EntityAnimationPacket.Animation.TAKE_DAMAGE
                )
            )
        }
    }

    override fun canJoin(newPlayers: Array<Player>): Boolean =
        (this.players.size + newPlayers.size) <= TEAM_AMOUNT * PLAYER_PER_TEAM && currentState == BedWarsGameState.WAITING

    override fun onJoin(joiningPlayer: Player) {
        teams.find { it.players.size < PLAYER_PER_TEAM }?.addPlayer(joiningPlayer)
            ?: teams.add(TeamTypes.createTeam(teams.size, uuid).also { it.addPlayer(joiningPlayer) })
    }

    override fun onLeave(leavingPlayer: Player) {
        leavingPlayer.getBedWarsTeam()?.removePlayer(leavingPlayer)
    }

    override fun shouldClose(): Boolean = this.players.size == 0

    override fun onClose() {

    }

}