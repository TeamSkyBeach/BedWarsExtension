package cc.lixou.bedwarsextension.game.team

import cc.lixou.bedwarsextension.game.BedWarsGame
import cc.lixou.stracciatella.game.GameManager.playingGame
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.color.DyeColor
import net.minestom.server.entity.Player
import world.cepi.kstom.Manager

class BedWarsTeam(name: String, prefix: Component, color: NamedTextColor, private val bedColor: DyeColor) {

    val players = mutableListOf<Player>()
    val team = Manager.team.createBuilder(name).prefix(prefix).teamColor(color).build()

    fun addPlayer(player: Player) {
        players.add(player)
        player.team = team
    }

}

/* Extensions */
fun Player.getBedWarsTeam(): BedWarsTeam? = playingGame()?.takeIf { it is BedWarsGame }
    ?.run { return (this as BedWarsGame).teams.find { it.players.contains(this@getBedWarsTeam) } }