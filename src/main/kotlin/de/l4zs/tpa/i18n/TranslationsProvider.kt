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

package de.l4zs.tpa.i18n

import de.l4zs.tpa.TPA
import de.l4zs.translations.Translation
import net.kyori.adventure.key.Key
import java.nio.file.Path
import java.util.Locale

class TranslationsProvider(plugin: TPA) {
    private val generalTranslations = Translation(plugin, Key.key("general"), Path.of("translations"), listOf(Locale.ENGLISH, Locale.GERMAN))

    fun reloadTranslations() {
        generalTranslations.reloadTranslations()
    }
}
