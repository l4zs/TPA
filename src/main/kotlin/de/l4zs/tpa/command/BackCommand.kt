package de.l4zs.tpa.command

import de.l4zs.tpa.listener.backLocation
import de.l4zs.tpa.util.Message
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.requiresPermission
import net.axay.kspigot.commands.runs
import org.bukkit.World

class BackCommand {

    fun register() = command("back") {
        requiresPermission("tpa.back")
        runs {
            if (player.backLocation != null) {
                if (player.backLocation!!.world.environment != player.world.environment && player.backLocation!!.world.environment != World.Environment.NORMAL) {
                    player.sendMessage(Message.backCannotNetherOrEnd())
                } else {
                    player.teleportAsync(player.backLocation!!)
                    player.sendMessage(Message.backTeleported())
                    player.backLocation = null
                }
            } else {
                player.sendMessage(Message.backNotSet())
            }
        }
    }
}
