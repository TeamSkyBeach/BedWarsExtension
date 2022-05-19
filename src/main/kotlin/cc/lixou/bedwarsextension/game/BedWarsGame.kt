package cc.lixou.bedwarsextension.game

import cc.lixou.bedwarsextension.game.generator.ResourceGenerator
import cc.lixou.bedwarsextension.game.generator.fabric.Generators
import cc.lixou.bedwarsextension.inventory.ShopInventory
import cc.lixou.stracciatella.game.Game
import net.minestom.server.entity.ItemEntity
import net.minestom.server.entity.Player
import net.minestom.server.event.item.ItemDropEvent
import net.minestom.server.event.item.PickupItemEvent
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.utils.time.TimeUnit
import world.cepi.kstom.Manager
import world.cepi.kstom.event.listenOnly

class BedWarsGame : Game() {

    private var state = BedWarsGameState.WAITING

    private val TEAM_AMOUNT = 8
    private val PLAYER_PER_TEAM = 1

    val teams = mutableListOf<BedWarsTeam>().also {
        for (i in 0..TEAM_AMOUNT) {
            it.add(BedWarsTeam())
        }
    }

    init {
        instance = Manager.instance.instances.first()
        // DEBUG
        eventNode.listenOnly<PlayerChatEvent> {
            if (message.lowercase() == "play") {
                state = BedWarsGameState.INGAME
            } else if (message.lowercase() == "shop") {
                player.openInventory(ShopInventory())
            } else if (message.lowercase() == "generator") {
                Generators.DIAMOND.build(player.instance!!, player.position, 1)
            }
        }
        // ENDDEBUG
        eventNode.listenOnly<ItemDropEvent> {
            when(state) {
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
            when(state) {
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
    }

    var currentState: BedWarsGameState = BedWarsGameState.WAITING

    override fun canJoin(newPlayers: Array<Player>): Boolean =
        (this.players.size + newPlayers.size) <= TEAM_AMOUNT * PLAYER_PER_TEAM

    override fun onJoin(joiningPlayer: Player) {
        teams.find { it.players.size < PLAYER_PER_TEAM }!!.players.add(joiningPlayer)
    }

    override fun onLeave(leavingPlayer: Player) {

    }

}