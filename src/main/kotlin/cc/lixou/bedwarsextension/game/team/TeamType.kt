package cc.lixou.bedwarsextension.game.team

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.color.DyeColor

data class TeamType(val baseName: String, val prefix: Component, val color: NamedTextColor, val bedColor: DyeColor)