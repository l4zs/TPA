package de.l4zs.tpa.util

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.axay.kspigot.main.KSpigotMainInstance
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.World
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

object TpaManager {

    val tpaNamespace = NamespacedKey(KSpigotMainInstance, "tpa")

    val tpaRequests = mutableMapOf<UUID, UUID>()
    val tpaHereRequests = mutableMapOf<UUID, UUID>()

    private fun isTpaAlreadyRequested(from: Player, to: Player): Boolean {
        return tpaRequests.containsKey(to.uniqueId) && tpaRequests[to.uniqueId] == from.uniqueId
    }

    private fun isTpaHereAlreadyRequested(from: Player, to: Player): Boolean {
        return tpaHereRequests.containsKey(to.uniqueId) && tpaHereRequests[to.uniqueId] == from.uniqueId
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
            tpaRequests[to.uniqueId] = from.uniqueId
            GlobalScope.launch {
                delay(60000)
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
            tpaHereRequests[to.uniqueId] = from.uniqueId
            GlobalScope.launch {
                delay(60000)
                tpaHereRequestExpire(from, to)
            }
        }
    }

    fun acceptTpaRequest(from: Player, to: Player, whoClicked: Player) {
        if (from == to) {
            whoClicked.sendMessage(Message.cannotTeleportToYourself())
            return
        }
        if (tpaRequests.containsKey(to.uniqueId) && tpaRequests[to.uniqueId] == from.uniqueId) {
            if (to.world.environment != from.world.environment && to.world.environment != World.Environment.NORMAL) {
                from.sendMessage(Message.cannotTeleportToDifferentDimension())
                to.sendMessage(Message.couldNotTeleport(from))
            } else {
                from.sendMessage(Message.tpaAcceptedFrom(to))
                to.sendMessage(Message.tpaAcceptedTo(from))
                from.teleportAsync(to.location)
            }
        } else if (tpaHereRequests.containsKey(to.uniqueId) && tpaHereRequests[to.uniqueId] == from.uniqueId) {
            if (to.world.environment != from.world.environment && from.world.environment != World.Environment.NORMAL) {
                to.sendMessage(Message.cannotTeleportToDifferentDimension())
                from.sendMessage(Message.couldNotTeleport(to))
            } else {
                from.sendMessage(Message.tpaHereAcceptedFrom(to))
                to.sendMessage(Message.tpaHereAcceptedTo(from))
                to.teleportAsync(from.location)
            }
        } else {
            whoClicked.sendMessage(Message.tpaRequestNotFoundReceiver(to))
        }
        tpaRequests.remove(to.uniqueId)
        tpaHereRequests.remove(to.uniqueId)
    }

    fun denyTpaRequest(from: Player, to: Player, whoClicked: Player) {
        if (from == to) {
            whoClicked.sendMessage(Message.cannotTeleportToYourself())
            return
        }
        if (tpaRequests.containsKey(to.uniqueId) && tpaRequests[to.uniqueId] == from.uniqueId) {
            from.sendMessage(Message.tpaDeniedFrom(to))
            to.sendMessage(Message.tpaDeniedTo(from))
        } else if (tpaHereRequests.containsKey(to.uniqueId) && tpaHereRequests[to.uniqueId] == from.uniqueId) {
            from.sendMessage(Message.tpaHereDeniedFrom(to))
            to.sendMessage(Message.tpaHereDeniedTo(from))
        } else {
            whoClicked.sendMessage(Message.tpaRequestNotFoundReceiver(to))
        }
        tpaRequests.remove(to.uniqueId)
        tpaHereRequests.remove(to.uniqueId)
    }

    fun cancelTpaRequest(from: Player, to: Player) {
        if (from == to) {
            from.sendMessage(Message.cannotTeleportToYourself())
            return
        }
        if (tpaRequests.containsKey(to.uniqueId) && tpaRequests[to.uniqueId] == from.uniqueId) {
            from.sendMessage(Message.tpaCancelledFrom(to))
            to.sendMessage(Message.tpaCancelledTo(from))
        } else if (tpaHereRequests.containsKey(to.uniqueId) && tpaHereRequests[to.uniqueId] == from.uniqueId) {
            from.sendMessage(Message.tpaHereCancelledFrom(to))
            to.sendMessage(Message.tpaHereCancelledTo(from))
        } else {
            from.sendMessage(Message.tpaRequestNotFoundSender(to))
        }
        tpaRequests.remove(to.uniqueId)
        tpaHereRequests.remove(to.uniqueId)
    }

    fun tpaToggle(player: Player) {
        player.tpa = !player.tpa
        player.sendMessage(Message.tpaToggle(player.tpa))
    }

    private fun tpaRequestExpire(from: Player, to: Player) {
        if (tpaRequests.containsKey(to.uniqueId) && tpaHereRequests[to.uniqueId] == from.uniqueId) {
            from.sendMessage(Message.tpaRequestExpired(to))
            tpaRequests.remove(to.uniqueId)
        }
    }

    private fun tpaHereRequestExpire(from: Player, to: Player) {
        if (tpaHereRequests.containsKey(to.uniqueId) && tpaHereRequests[to.uniqueId] == from.uniqueId) {
            from.sendMessage(Message.tpaHereRequestExpired(to))
            tpaHereRequests.remove(to.uniqueId)
        }
    }
}
