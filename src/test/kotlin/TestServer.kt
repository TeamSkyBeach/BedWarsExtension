import cc.lixou.bedwarsextension.game.BedWarsGame
import cc.lixou.stracciatella.Stracciatella
import cc.lixou.stracciatella.game.GameManager
import net.minestom.server.event.player.PlayerLoginEvent
import world.cepi.kstom.Manager
import world.cepi.kstom.event.listenOnly

fun main() {
    Stracciatella().start()
    Manager.globalEvent.listenOnly<PlayerLoginEvent> {
        GameManager.joinGame<BedWarsGame>(arrayOf(this.player))
    }
}