package de.l4zs.tpa.config

import de.l4zs.tpa.TPA
import de.l4zs.tpa.config.impl.Config

class ConfigManager(private val plugin: TPA) {

    lateinit var config: Config

    init {
        reloadConfigs()
    }

    fun reloadConfigs() {
        config = Config()
    }
}
