package cc.lixou.bedwarsextension

import cc.lixou.bedwarsextension.game.BedWarsGame
import cc.lixou.stracciatella.game.GameManager
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.extensions.Extension
import world.cepi.kstom.Manager
import world.cepi.kstom.event.listenOnly

class BedWarsExtension : Extension() {

    override fun initialize() {
        GameManager.registerGame<BedWarsGame>()
        Manager.globalEvent.listenOnly<PlayerLoginEvent> {
            GameManager.joinGame<BedWarsGame>(arrayOf(this.player))
        }
        println("[BedWars] Initialized successfully")
    }

    override fun terminate() {
        println("[BedWars] Terminated successfully")
    }

}