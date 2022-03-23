package de.l4zs.tpa.command

import de.l4zs.tpa.util.TpaManager
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.requiresPermission
import net.axay.kspigot.commands.runs
import net.axay.kspigot.extensions.onlinePlayers

class TpahereallCommand {

    fun register() = command("tpahereall") {
        requiresPermission("tpa.tpahereall")
        runs {
            onlinePlayers.minus(player).forEach {
                TpaManager.sendTpaHereRequest(player, it)
            }
        }
    }
}
