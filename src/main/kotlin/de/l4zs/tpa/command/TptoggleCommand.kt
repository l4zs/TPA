package de.l4zs.tpa.command

import de.l4zs.tpa.util.TpaManager
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.requiresPermission
import net.axay.kspigot.commands.runs

class TptoggleCommand {

    fun register() = command("tptoggle") {
        requiresPermission("tpa.tptoggle")
        runs {
            TpaManager.tpaToggle(player)
        }
    }
}
