package de.l4zs.tpa.command

import com.mojang.brigadier.arguments.StringArgumentType
import de.l4zs.tpa.TPA
import net.axay.kspigot.commands.argument
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.getArgument
import net.axay.kspigot.commands.literal
import net.axay.kspigot.commands.requiresPermission
import net.axay.kspigot.commands.runs
import net.axay.kspigot.commands.suggestListSuspending
import net.axay.kspigot.extensions.onlinePlayers
import net.kyori.adventure.text.Component

class TpaCommand {

    fun register(plugin: TPA) = command("tpa") {
        requiresPermission("tpa.tpa")
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
                plugin.tpaManager.sendTpaRequest(player, target)
            }
        }
        literal("reload-config") {
            requiresPermission("tpa.reload-config")
            runs {
                plugin.configManager.reloadConfigs()
                player.sendMessage(Component.text("Reloaded config"))
            }
        }
        literal("reload-translations") {
            requiresPermission("tpa.reload-translations")
            runs {
                plugin.reloadTranslations()
                player.sendMessage(Component.text("Reloaded translations"))
            }
        }
    }
}
