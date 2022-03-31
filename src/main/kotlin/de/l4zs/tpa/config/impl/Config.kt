package de.l4zs.tpa.config.impl

import de.l4zs.tpa.config.AbstractConfig

class Config : AbstractConfig("config.yml") {

    val yml = yaml
}
