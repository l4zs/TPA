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

package de.l4zs.tpa.config.impl

import de.l4zs.tpa.TPA
import de.l4zs.tpa.config.AbstractConfig
import net.axay.kspigot.extensions.server
import org.bukkit.World
import java.util.Locale

class Config(plugin: TPA) : AbstractConfig("config.yml", plugin) {

    val requestsExpire: Long
        get() = yaml.getLong("request_expire", 60) * 1000
    private val disabledBackWorlds: List<World>
        get() = yaml.getStringList("disabled_worlds.back").mapNotNull { server.getWorld(it) }
    private val disabledTpaToWorlds: List<World>
        get() = yaml.getStringList("disabled_worlds.tpa.to").mapNotNull { server.getWorld(it) }
    private val disabledTpaFromWorlds: List<World>
        get() = yaml.getStringList("disabled_worlds.tpa.from").mapNotNull { server.getWorld(it) }
    private val allowedTpaIfSameWorld: List<World>
        get() = yaml.getStringList("disabled_worlds.tpa.allow_if_same_world").mapNotNull { server.getWorld(it) }
    private val alwaysAllowTpaIfSameWorld: Boolean
        get() = yaml.getStringList("disabled_worlds.tpa.allow_if_same_world").contains("*")
    val locales: List<Locale>
        get() = yaml.getStringList("locales").mapNotNull { Locale.forLanguageTag(it) }
    val disabledCommands: List<String>
        get() = yaml.getStringList("disabled_commands")

    fun isWorldBackDisabled(name: String): Boolean {
        return disabledBackWorlds.any { it.name == name }
    }

    fun shouldAllowTpa(from: World, to: World): Boolean {
        return if (from == to && (alwaysAllowTpaIfSameWorld || allowedTpaIfSameWorld.any { it == from })) true
        else !(disabledTpaFromWorlds.contains(from) || disabledTpaToWorlds.contains(to))
    }
}
