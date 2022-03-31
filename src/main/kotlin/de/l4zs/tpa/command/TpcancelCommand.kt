/*
 *     TPA is a simple plugin giving the ability to request teleports to others and go back to your death location if you die.
 *     Copyright (c) 2022 l4zs
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.l4zs.tpa.command

import com.mojang.brigadier.arguments.StringArgumentType
import de.l4zs.tpa.TPA
import net.axay.kspigot.commands.argument
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.getArgument
import net.axay.kspigot.commands.requiresPermission
import net.axay.kspigot.commands.runs
import net.axay.kspigot.commands.suggestListSuspending
import net.axay.kspigot.extensions.onlinePlayers
import net.kyori.adventure.text.Component

class TpcancelCommand {

    fun register(plugin: TPA) = command("tpcancel") {
        requiresPermission("tpa.tpcancel")
        argument("player", StringArgumentType.greedyString()) {
            suggestListSuspending { suggest ->
                onlinePlayers.filter {
                    if (it.name == suggest.source.playerOrException.name.contents) {
                        false
                    } else if (suggest.input != null && suggest.input.substring(suggest.input.length - 1) != " ") {
                        it.name.lowercase().startsWith(suggest.getArgument<String>("player").lowercase())
                    } else {
                        true
                    }
                }.map { it.name }.sorted()
            }
            runs {
                val target = onlinePlayers.firstOrNull { it.name == getArgument<String>("player") }
                    ?: return@runs player.sendMessage(
                        Component.translatable("player_not_found")
                            .args(Component.text(getArgument<String>("player")))
                    )
                plugin.tpaManager.cancelTpaRequest(player, target)
            }
        }
    }
}
