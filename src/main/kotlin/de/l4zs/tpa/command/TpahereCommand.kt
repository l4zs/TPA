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

class TpahereCommand {

    fun register(plugin: TPA) = command("tpahere") {
        requiresPermission("tpa.tpahere")
        argument("player", StringArgumentType.greedyString()) {
            suggestListSuspending { suggest ->
                onlinePlayers.filter {
                    suggest.input != null && it.name.startsWith(suggest.getArgument<String>("player"))
                }.map { it.name }.filter {
                    if (it == suggest.source.playerOrException.name.contents) {
                        false
                    } else if (suggest.input != null && suggest.input.substring(suggest.input.length - 1) != " ") {
                        it.lowercase().startsWith(suggest.getArgument<String>("player").lowercase())
                    } else {
                        true
                    }
                }.sorted()
            }
            runs {
                val target = onlinePlayers.firstOrNull { it.name == getArgument<String>("player") }
                    ?: return@runs player.sendMessage(
                        Component.translatable("player_not_found")
                            .args(Component.text(getArgument<String>("player")))
                    )
                plugin.tpaManager.sendTpaHereRequest(player, target)
            }
        }
    }
}
