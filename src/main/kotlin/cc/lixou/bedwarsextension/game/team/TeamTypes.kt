package cc.lixou.bedwarsextension.game.team

import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.color.DyeColor
import world.cepi.kstom.adventure.asMini
import java.util.UUID

object TeamTypes {

    val RED = TeamType("red", "<red><bold>R <reset>".asMini(), NamedTextColor.RED, DyeColor.RED)

    val BLUE = TeamType("blue", "<blue><bold>B <reset>".asMini(), NamedTextColor.BLUE, DyeColor.BLUE)

    val GREEN = TeamType("green", "<green><bold>G <reset>".asMini(), NamedTextColor.GREEN, DyeColor.GREEN)

    val YELLOW = TeamType("yellow", "<yellow><bold>Y <reset>".asMini(), NamedTextColor.YELLOW, DyeColor.YELLOW)

    val AQUA = TeamType("aqua", "<aqua><bold>A <reset>".asMini(), NamedTextColor.AQUA, DyeColor.CYAN)

    val WHITE = TeamType("white", "<white><bold>W <reset>".asMini(), NamedTextColor.WHITE, DyeColor.WHITE)

    val PINK = TeamType("pink", "<light_purple><bold>P <reset>".asMini(), NamedTextColor.LIGHT_PURPLE, DyeColor.PINK)

    val GRAY = TeamType("gray", "<gray><bold>S <reset>".asMini(), NamedTextColor.GRAY, DyeColor.GRAY)

    private val entries = listOf(RED, BLUE, GREEN, YELLOW, AQUA, WHITE, PINK, GRAY)

    fun createTeam(nextFree: Int, uuid: UUID): BedWarsTeam {
        val next: TeamType = entries[nextFree]
        return BedWarsTeam(nextFree.toString() + next.baseName + uuid, next.prefix, next.color, next.bedColor)
    }

}