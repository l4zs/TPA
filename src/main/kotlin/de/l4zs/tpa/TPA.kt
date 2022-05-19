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

package de.l4zs.tpa

import de.l4zs.tpa.command.BackCommand
import de.l4zs.tpa.command.TpaCommand
import de.l4zs.tpa.command.TpacceptCommand
import de.l4zs.tpa.command.TpahereCommand
import de.l4zs.tpa.command.TpahereallCommand
import de.l4zs.tpa.command.TpcancelCommand
import de.l4zs.tpa.command.TpdenyCommand
import de.l4zs.tpa.command.TptoggleCommand
import de.l4zs.tpa.config.ConfigManager
import de.l4zs.tpa.i18n.TranslationsProvider
import de.l4zs.tpa.listener.BackListener
import de.l4zs.tpa.util.TpaManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.axay.kspigot.main.KSpigot

class TPA : KSpigot() {

    lateinit var translationsProvider: TranslationsProvider
    lateinit var configManager: ConfigManager
    lateinit var tpaManager: TpaManager
    val defaultScope = CoroutineScope(Dispatchers.Default)
    val ioScope = CoroutineScope(Dispatchers.IO)

    override fun startup() {
        configManager = ConfigManager(this)
        translationsProvider = TranslationsProvider(this)
        tpaManager = TpaManager(this)

        listOf(
            BackCommand(),
            TpacceptCommand(),
            TpaCommand(),
            TpahereCommand(),
            TpahereallCommand(),
            TpcancelCommand(),
            TpdenyCommand(),
            TptoggleCommand()
        ).filter { !configManager.config.disabledCommands.contains(it.commandName) }.map { it.register(this) }

        BackListener().register()
    }
}
