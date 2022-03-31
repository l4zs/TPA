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

package de.l4zs.tpa.listener

import net.axay.kspigot.event.listen
import net.axay.kspigot.main.KSpigotMainInstance
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import java.util.UUID

val LOCATION_TAG_TYPE = LocationTagType()

class LocationTagType : PersistentDataType<PersistentDataContainer, Location> {
    private val worldKey: NamespacedKey = NamespacedKey(KSpigotMainInstance, "world_uuid")
    private val xKey: NamespacedKey = NamespacedKey(KSpigotMainInstance, "x")
    private val yKey: NamespacedKey = NamespacedKey(KSpigotMainInstance, "y")
    private val zKey: NamespacedKey = NamespacedKey(KSpigotMainInstance, "z")
    private val pitchKey: NamespacedKey = NamespacedKey(KSpigotMainInstance, "pitch")
    private val yawKey: NamespacedKey = NamespacedKey(KSpigotMainInstance, "yaw")

    override fun getPrimitiveType(): Class<PersistentDataContainer> {
        return PersistentDataContainer::class.java
    }

    override fun getComplexType(): Class<Location> {
        return Location::class.java
    }

    override fun toPrimitive(complex: Location, context: PersistentDataAdapterContext): PersistentDataContainer {
        val container = context.newPersistentDataContainer()
        container.set(worldKey, PersistentDataType.STRING, complex.world.uid.toString())
        container.set(xKey, PersistentDataType.DOUBLE, complex.x)
        container.set(yKey, PersistentDataType.DOUBLE, complex.y)
        container.set(zKey, PersistentDataType.DOUBLE, complex.z)
        container.set(pitchKey, PersistentDataType.FLOAT, complex.pitch)
        container.set(yawKey, PersistentDataType.FLOAT, complex.yaw)
        return container
    }

    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext): Location {
        val worldUuid = UUID.fromString(primitive.get(worldKey, PersistentDataType.STRING))
        val x = primitive.get(xKey, PersistentDataType.DOUBLE)
        val y = primitive.get(yKey, PersistentDataType.DOUBLE)
        val z = primitive.get(zKey, PersistentDataType.DOUBLE)
        val pitch = primitive.get(pitchKey, PersistentDataType.FLOAT)
        val yaw = primitive.get(yawKey, PersistentDataType.FLOAT)
        return Location(KSpigotMainInstance.server.getWorld(worldUuid), x ?: 0.0, y ?: 0.0, z ?: 0.0, yaw ?: 0F, pitch ?: 0F)
    }
}

internal val backNamespace = NamespacedKey(KSpigotMainInstance, "back")

var Player.backLocation: Location?
    get() = if (persistentDataContainer.has(backNamespace)) {
        persistentDataContainer.get(backNamespace, LOCATION_TAG_TYPE)
    } else {
        null
    }
    set(value) {
        if (value == null) {
            persistentDataContainer.remove(backNamespace)
        } else {
            persistentDataContainer.set(backNamespace, LOCATION_TAG_TYPE, value)
        }
    }

class BackListener {

    fun register() = listen<PlayerDeathEvent> {
        it.player.backLocation = it.player.location
    }
}
