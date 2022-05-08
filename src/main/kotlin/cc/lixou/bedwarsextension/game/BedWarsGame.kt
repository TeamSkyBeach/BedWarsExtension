package cc.lixou.bedwarsextension.game

import cc.lixou.bedwarsextension.inventory.ShopInventory
import cc.lixou.stracciatella.game.Game
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import world.cepi.kstom.event.listenOnly

class BedWarsGame : Game() {

    companion object {
        val MAX_PLAYERS = 8
    }

    init {
        eventNode.listenOnly<PlayerChatEvent> {
            if(message.lowercase() == "shop") {
                player.openInventory(ShopInventory().inventory)
            }
        }
    }

    var currentState: BedWarsGameState = BedWarsGameState.WAITING

    override fun canJoin(players: Array<Player>): Boolean = (this.players.size + players.size) >= MAX_PLAYERS

    override fun onJoin(player: Player) {
        player.inventory.setItemStack(0, ItemStack.of(Material.WOODEN_SWORD))
    }

    override fun onLeave(player: Player) {
        player.sendMessage("bye bye")
    }

}