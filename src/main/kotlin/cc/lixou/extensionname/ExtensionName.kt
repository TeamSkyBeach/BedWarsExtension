package cc.lixou.extensionname

import net.minestom.server.extensions.Extension

class ExtensionName : Extension() {

    override fun initialize() {
        println("[BedWars] Initialized successfully")
    }

    override fun terminate() {
        println("[BedWars] Terminated successfully")
    }

}