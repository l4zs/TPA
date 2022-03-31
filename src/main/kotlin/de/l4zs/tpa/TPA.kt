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
import net.axay.kspigot.commands.register
import net.axay.kspigot.event.register
import net.axay.kspigot.extensions.pluginManager
import net.axay.kspigot.main.KSpigot
import java.util.Locale
import kotlin.io.path.div
import kotlin.io.path.notExists

class TPA : KSpigot() {

    lateinit var translationsProvider: TranslationsProvider
    lateinit var configManager: ConfigManager
    lateinit var tpaManager: TpaManager

    override fun startup() {
        BackCommand().register().register(true)
        TpacceptCommand().register(this).register(true)
        TpaCommand().register(this).register(true)
        TpahereCommand().register(this).register(true)
        TpahereallCommand().register(this).register(true)
        TpcancelCommand().register(this).register(true)
        TpdenyCommand().register(this).register(true)
        TptoggleCommand().register().register(true)

        BackListener().register().register()

        val translationNames = listOf("general_en.properties", "general_de.properties")
        // save default translations
        translationNames.forEach {
            if ((dataFolder.toPath() / "translations" / it).notExists()) {
                saveResource("translations/$it", false)
            }
        }

        configManager = ConfigManager(this)
        translationsProvider = TranslationsProvider(this)
        reloadTranslations()
        tpaManager = TpaManager(this)
    }

    override fun shutdown() {
    }

    fun reloadTranslations() {
        val locales = configManager.config.yml.getStringList("translations").map { Locale(it) }

        if (locales.isEmpty()) {
            logger.severe("No translations found. Please add at least one translation (default available are 'en' and 'de') to the config.yml")
            pluginManager.disablePlugin(this)
        }

        translationsProvider.unregisterTranslations(locales.first())
        translationsProvider.locales.clear()
        translationsProvider.locales.addAll(locales)
        translationsProvider.registerTranslations()
    }
}
