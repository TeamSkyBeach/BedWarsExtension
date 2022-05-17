package cc.lixou.bedwarsextension.game

import cc.lixou.bedwarsextension.game.generator.ResourceGenerator
import cc.lixou.bedwarsextension.inventory.ShopInventory
import cc.lixou.stracciatella.game.Game
import net.minestom.server.entity.ItemEntity
import net.minestom.server.entity.Player
import net.minestom.server.event.item.ItemDropEvent
import net.minestom.server.event.item.PickupItemEvent
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.utils.time.TimeUnit
import world.cepi.kstom.Manager
import world.cepi.kstom.adventure.asMini
import world.cepi.kstom.event.listenOnly


class BedWarsGame : Game() {

    companion object {
        val MAX_PLAYERS = 8
    }

    val state = initGameState(BedWarsGameState.WAITING)

    init {
        instance = Manager.instance.instances.first()
        // DEBUG
        eventNode.listenOnly<PlayerChatEvent> {
            if (message.lowercase() == "shop") {
                player.openInventory(ShopInventory())
            } else if(message.lowercase() == "generator") {
                ResourceGenerator(player.instance!!, player.position, Material.DIAMOND_BLOCK, Material.DIAMOND, {lvl -> "<gradient:dark_aqua:aqua><bold>Diamond $lvl".asMini()}, 5)
            }
        }
        // ENDDEBUG
        state.eventNode(BedWarsGameState.INGAME).listenOnly<ItemDropEvent> {
            val itemEntity = ItemEntity(itemStack)
            itemEntity.setPickupDelay(40, TimeUnit.SERVER_TICK)
            itemEntity.setInstance(instance, player.position.add(0.0, 1.5, 0.0))
            itemEntity.isGlowing = true // TODO: Glowing in team color
            itemEntity.velocity = player.position.direction().mul(6.0)
        }
        state.eventNode(BedWarsGameState.INGAME).listenOnly<PickupItemEvent> {
            if (entity is Player) {
                (entity as Player).inventory.addItemStack(itemStack)
            } else {
                isCancelled = true
            }
        }
    }

    var currentState: BedWarsGameState = BedWarsGameState.WAITING

    override fun canJoin(newPlayers: Array<Player>): Boolean = (this.players.size + newPlayers.size) >= MAX_PLAYERS

    override fun onJoin(joiningPlayer: Player) {
        joiningPlayer.inventory.setItemStack(0, ItemStack.of(Material.WOODEN_SWORD))
    }

    override fun onLeave(leavingPlayer: Player) {
        leavingPlayer.sendMessage("bye bye")
    }

}