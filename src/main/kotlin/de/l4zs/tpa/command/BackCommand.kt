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

import de.l4zs.tpa.TPA
import de.l4zs.tpa.listener.backLocation
import de.l4zs.tpa.util.Message
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.requiresPermission
import net.axay.kspigot.commands.runs

class BackCommand : RegisterableCommand {

    override val commandName = "back"

    override fun register(plugin: TPA) = command(commandName) {
        requiresPermission("tpa.back")
        runs {
            if (player.backLocation != null) {
                if (plugin.configManager.config.isWorldBackDisabled(player.backLocation!!.world.name)) {
                    player.sendMessage(Message.backCannotTeleportToThatWorld())
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
