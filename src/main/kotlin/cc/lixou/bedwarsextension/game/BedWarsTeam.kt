package cc.lixou.bedwarsextension.game

import cc.lixou.stracciatella.game.GameManager.playingGame
import net.minestom.server.entity.Player

class BedWarsTeam {

    val players = mutableListOf<Player>()

}

/* Extensions */
fun Player.getBedWarsTeam(): BedWarsTeam? = playingGame()?.takeIf { it is BedWarsGame }?.run { return (this as BedWarsGame).teams.find { it.players.contains(this@getBedWarsTeam) } }