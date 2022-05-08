package cc.lixou.bedwarsextension

import cc.lixou.bedwarsextension.game.BedWarsGame
import cc.lixou.stracciatella.game.GameManager
import net.minestom.server.extensions.Extension

class BedWarsExtension : Extension() {

    override fun initialize() {
        GameManager.registerGame<BedWarsGame>()
        println("[BedWars] Initialized successfully")
    }

    override fun terminate() {
        println("[BedWars] Terminated successfully")
    }

}