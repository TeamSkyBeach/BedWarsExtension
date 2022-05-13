import cc.lixou.stracciatella.Stracciatella
import cc.lixou.stracciatella.game.GameManager
import net.minestom.server.event.player.PlayerLoginEvent
import world.cepi.kstom.Manager
import world.cepi.kstom.event.listenOnly

fun main() {
    val server = Stracciatella()
    /*Manager.globalEvent.listenOnly<PlayerLoginEvent> {
        GameManager.joinGame<MyGame>(arrayOf(this.player))
    }*/
    server.start()
}