package de.l4zs.tpa

import de.l4zs.tpa.command.BackCommand
import de.l4zs.tpa.command.TpaCommand
import de.l4zs.tpa.command.TpacceptCommand
import de.l4zs.tpa.command.TpahereCommand
import de.l4zs.tpa.command.TpahereallCommand
import de.l4zs.tpa.command.TpcancelCommand
import de.l4zs.tpa.command.TpdenyCommand
import de.l4zs.tpa.command.TptoggleCommand
import de.l4zs.tpa.i18n.TranslationsProvider
import de.l4zs.tpa.listener.BackListener
import net.axay.kspigot.commands.register
import net.axay.kspigot.event.register
import net.axay.kspigot.main.KSpigot

class TPA : KSpigot() {

    private lateinit var translationsProvider: TranslationsProvider

    override fun startup() {
        BackCommand().register().register(true)
        TpacceptCommand().register().register(true)
        TpaCommand().register().register(true)
        TpahereCommand().register().register(true)
        TpahereallCommand().register().register(true)
        TpcancelCommand().register().register(true)
        TpdenyCommand().register().register(true)
        TptoggleCommand().register().register(true)

        BackListener().register().register()

        translationsProvider = TranslationsProvider()
        translationsProvider.registerTranslations()
    }

    override fun shutdown() {
    }
}
