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
import net.axay.kspigot.extensions.pluginManager
import net.kyori.adventure.key.Key
import net.kyori.adventure.translation.GlobalTranslator
import net.kyori.adventure.translation.TranslationRegistry
import java.io.File
import java.net.MalformedURLException
import java.net.URLClassLoader
import java.util.Locale
import java.util.ResourceBundle
import kotlin.io.path.div
import kotlin.io.path.notExists

class TranslationsProvider(private val plugin: TPA) {

    private val key = Key.key("de.l4zs.tpa")

    private var translationRegistry = TranslationRegistry.create(key).apply {
        defaultLocale(Locale.ENGLISH)
    }

    private val defaultTranslations = listOf(Locale.ENGLISH, Locale.GERMAN)
    private val locales = mutableListOf<Locale>()

    init {
        reloadTranslations()
    }

    fun reloadTranslations() {
        saveDefaultTranslationsIfNotExists()
        loadTranslations()
    }

    private fun saveDefaultTranslationsIfNotExists() {
        defaultTranslations.forEach {
            if ((plugin.dataFolder.toPath() / "translations" / "general_${it.language}.properties").notExists()) {
                plugin.saveResource("translations/general_${it.language}.properties", false)
            }
        }
    }

    private fun loadTranslations() {
        val locales = plugin.configManager.config.locales

        if (locales.isEmpty()) {
            plugin.logger.severe("No translations found. Please add at least one translation (default available are 'en' and 'de') to the config.yml")
            pluginManager.disablePlugin(plugin)
        }

        unregisterTranslations(locales.first())
        this.locales.clear()
        this.locales.addAll(locales)
        registerTranslations()
    }

    private fun unregisterTranslations(defaultLocale: Locale) {
        if (GlobalTranslator.translator().sources().contains(translationRegistry)) {
            GlobalTranslator.translator().removeSource(translationRegistry)
            translationRegistry = TranslationRegistry.create(key).apply {
                defaultLocale(defaultLocale)
            }
        }
    }

    private fun registerTranslations() {
        locales.forEach {
            translationRegistry.registerAll(
                it,
                resourceBundleFromClassLoader(plugin.dataFolder.resolve("translations").path, "general", it),
                false
            )
        }
        GlobalTranslator.translator().addSource(translationRegistry)
    }
}

@Throws(MalformedURLException::class)
@Suppress("SameParameterValue")
private fun resourceBundleFromClassLoader(dir: String, bundleName: String, locale: Locale): ResourceBundle {
    val file = File(dir)
    val urls = arrayOf(file.toURI().toURL())
    val loader: ClassLoader = URLClassLoader(urls)
    return ResourceBundle.getBundle(bundleName, locale, loader)
}
