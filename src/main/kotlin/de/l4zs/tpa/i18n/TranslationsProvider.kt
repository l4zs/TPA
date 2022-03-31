package de.l4zs.tpa.i18n

import de.l4zs.tpa.TPA
import net.kyori.adventure.key.Key
import net.kyori.adventure.translation.GlobalTranslator
import net.kyori.adventure.translation.TranslationRegistry
import java.io.File
import java.net.MalformedURLException
import java.net.URLClassLoader
import java.util.Locale
import java.util.ResourceBundle

class TranslationsProvider(private val plugin: TPA) {

    private val key = Key.key("de.l4zs.tpa")

    private var translationRegistry = TranslationRegistry.create(key).apply {
        defaultLocale(Locale.ENGLISH)
    }

    val locales = mutableListOf<Locale>()

    fun setDefault(locale: Locale) {
        translationRegistry.defaultLocale(locale)
    }

    fun unregisterTranslations(defaultLocale: Locale) {
        if (GlobalTranslator.translator().sources().contains(translationRegistry)) {
            GlobalTranslator.translator().removeSource(translationRegistry)
            translationRegistry = TranslationRegistry.create(key).apply {
                defaultLocale(defaultLocale)
            }
        }
    }

    fun registerTranslations() {
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
private fun resourceBundleFromClassLoader(dir: String, bundleName: String, locale: Locale): ResourceBundle {
    val file = File(dir)
    val urls = arrayOf(file.toURI().toURL())
    val loader: ClassLoader = URLClassLoader(urls)
    return ResourceBundle.getBundle(bundleName, locale, loader)
}
