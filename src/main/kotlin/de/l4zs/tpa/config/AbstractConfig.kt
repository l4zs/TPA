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

package de.l4zs.tpa.config

import net.axay.kspigot.main.KSpigotMainInstance
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

abstract class AbstractConfig(name: String) {

    private val file: File
    private val dir: File = File(KSpigotMainInstance.dataFolder.path)
    protected val yaml: YamlConfiguration

    init {
        if (!dir.exists()) {
            dir.mkdirs()
        }
        file = File(dir, name)
        if (!file.exists()) {
            KSpigotMainInstance.saveResource(name, false)
        }
        yaml = YamlConfiguration()
        try {
            yaml.load(file)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun save() {
        try {
            yaml.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
