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

package de.l4zs.tpa.util

import de.l4zs.tpa.TPA
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.axay.kspigot.extensions.pluginKey
import net.axay.kspigot.utils.OnlinePlayerMap
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import java.util.UUID

fun Player.ping() = playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
var Player.tpa: Boolean
    get() = if (persistentDataContainer.has(TpaManager.tpaNamespace)) {
        persistentDataContainer.get(TpaManager.tpaNamespace, PersistentDataType.BYTE) == 1.toByte()
    } else {
        true
    }
    set(value) {
        persistentDataContainer.set(TpaManager.tpaNamespace, PersistentDataType.BYTE, if (value) 1.toByte() else 0.toByte())
    }

fun Player.toggleTpa() {
    tpa = !tpa
    sendMessage(Message.tpaToggle(tpa))
}

class TpaManager(private val plugin: TPA) {

    companion object {
        val tpaNamespace = pluginKey("tpa")
    }

    private val tpaRequests = OnlinePlayerMap<UUID>()
    private val tpaHereRequests = OnlinePlayerMap<UUID>()

    private fun isTpaAlreadyRequested(from: Player, to: Player): Boolean {
        return tpaRequests.internalMap.containsKey(to.uniqueId) && tpaRequests[to] == from.uniqueId
    }

    private fun isTpaHereAlreadyRequested(from: Player, to: Player): Boolean {
        return tpaHereRequests.internalMap.containsKey(to.uniqueId) && tpaHereRequests[to] == from.uniqueId
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun sendTpaRequest(from: Player, to: Player) {
        if (from == to) {
            from.sendMessage(Message.cannotTeleportToYourself())
            return
        }
        if (!to.tpa) {
            from.sendMessage(Message.playerDoesNotAcceptTpaRequests(to))
            return
        }
        if (isTpaAlreadyRequested(from, to)) {
            from.sendMessage(Message.tpaAlreadyRequested(to))
            return
        } else {
            from.sendMessage(Message.tpaRequestSent(to))
            to.ping()
            to.sendMessage(Message.tpaRequestReceived(from))
            tpaRequests[to] = from.uniqueId
            GlobalScope.launch {
                delay(plugin.configManager.config.yml.getLong("request_expire", 60) * 1000)
                tpaRequestExpire(from, to)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun sendTpaHereRequest(from: Player, to: Player) {
        if (from == to) {
            from.sendMessage(Message.cannotTeleportToYourself())
            return
        }
        if (!to.tpa) {
            from.sendMessage(Message.playerDoesNotAcceptTpaRequests(to))
            return
        }
        if (isTpaHereAlreadyRequested(from, to)) {
            from.sendMessage(Message.tpaHereAlreadyRequested(to))
            return
        } else {
            from.sendMessage(Message.tpaHereRequestSent(to))
            to.ping()
            to.sendMessage(Message.tpaHereRequestReceived(from))
            tpaHereRequests[to] = from.uniqueId
            GlobalScope.launch {
                delay(60000)
                tpaHereRequestExpire(from, to)
            }
        }
    }

    fun acceptTpaRequest(from: Player, to: Player, whoClicked: Player) {
        val whoNotClicked = if (whoClicked == from) to else from
        if (from == to) {
            whoClicked.sendMessage(Message.cannotTeleportToYourself())
            return
        }
        if (tpaRequests.internalMap.containsKey(to.uniqueId) && tpaRequests[to] == from.uniqueId) {
            if ((
                plugin.configManager.config.yml.getStringList("disabled_worlds.tpa.to").contains(whoClicked.world.name) ||
                    plugin.configManager.config.yml.getStringList("disabled_worlds.tpa.from").contains(whoNotClicked.world.name)
                ) &&
                !(
                    (
                        plugin.configManager.config.yml.getStringList("disabled_worlds.tpa.allow_if_same_world").contains(to.world.name) &&
                            to.world == from.world
                        ) || plugin.configManager.config.yml.getStringList("disabled_worlds.tpa.allow_if_same_world").contains("*")
                    )
            ) {
                from.sendMessage(Message.cannotTeleportToThatWorld())
                to.sendMessage(Message.couldNotTeleport(from))
            } else {
                from.sendMessage(Message.tpaAcceptedFrom(to))
                to.sendMessage(Message.tpaAcceptedTo(from))
                from.teleportAsync(to.location)
            }
        } else if (tpaHereRequests.internalMap.containsKey(to.uniqueId) && tpaHereRequests[to] == from.uniqueId) {
            if ((
                plugin.configManager.config.yml.getStringList("disabled_worlds.tpa.to").contains(whoNotClicked.world.name) ||
                    plugin.configManager.config.yml.getStringList("disabled_worlds.tpa.from").contains(whoClicked.world.name)
                ) &&
                !(
                    (
                        plugin.configManager.config.yml.getStringList("disabled_worlds.tpa.allow_if_same_world").contains(to.world.name) &&
                            to.world == from.world
                        ) || plugin.configManager.config.yml.getStringList("disabled_worlds.tpa.allow_if_same_world").contains("*")
                    )
            ) {
                to.sendMessage(Message.cannotTeleportToThatWorld())
                from.sendMessage(Message.couldNotTeleport(to))
            } else {
                from.sendMessage(Message.tpaHereAcceptedFrom(to))
                to.sendMessage(Message.tpaHereAcceptedTo(from))
                to.teleportAsync(from.location)
            }
        } else {
            whoClicked.sendMessage(Message.tpaRequestNotFoundReceiver(to))
        }
        tpaRequests.minusAssign(to)
        tpaHereRequests.minusAssign(to)
    }

    fun denyTpaRequest(from: Player, to: Player, whoClicked: Player) {
        if (from == to) {
            whoClicked.sendMessage(Message.cannotTeleportToYourself())
            return
        }
        if (tpaRequests.internalMap.containsKey(to.uniqueId) && tpaRequests[to] == from.uniqueId) {
            from.sendMessage(Message.tpaDeniedFrom(to))
            to.sendMessage(Message.tpaDeniedTo(from))
        } else if (tpaHereRequests.internalMap.containsKey(to.uniqueId) && tpaHereRequests[to] == from.uniqueId) {
            from.sendMessage(Message.tpaHereDeniedFrom(to))
            to.sendMessage(Message.tpaHereDeniedTo(from))
        } else {
            whoClicked.sendMessage(Message.tpaRequestNotFoundReceiver(to))
        }
        tpaRequests.minusAssign(to)
        tpaHereRequests.minusAssign(to)
    }

    fun cancelTpaRequest(from: Player, to: Player) {
        if (from == to) {
            from.sendMessage(Message.cannotTeleportToYourself())
            return
        }
        if (tpaRequests.internalMap.containsKey(to.uniqueId) && tpaRequests[to] == from.uniqueId) {
            from.sendMessage(Message.tpaCancelledFrom(to))
            to.sendMessage(Message.tpaCancelledTo(from))
        } else if (tpaHereRequests.internalMap.containsKey(to.uniqueId) && tpaHereRequests[to] == from.uniqueId) {
            from.sendMessage(Message.tpaHereCancelledFrom(to))
            to.sendMessage(Message.tpaHereCancelledTo(from))
        } else {
            from.sendMessage(Message.tpaRequestNotFoundSender(to))
        }
        tpaRequests.minusAssign(to)
        tpaHereRequests.minusAssign(to)
    }

    private fun tpaRequestExpire(from: Player, to: Player) {
        if (tpaRequests.internalMap.containsKey(to.uniqueId) && tpaHereRequests[to] == from.uniqueId) {
            from.sendMessage(Message.tpaRequestExpired(to))
            tpaRequests.minusAssign(to)
        }
    }

    private fun tpaHereRequestExpire(from: Player, to: Player) {
        if (tpaHereRequests.internalMap.containsKey(to.uniqueId) && tpaHereRequests[to] == from.uniqueId) {
            from.sendMessage(Message.tpaHereRequestExpired(to))
            tpaHereRequests.minusAssign(to)
        }
    }
}
