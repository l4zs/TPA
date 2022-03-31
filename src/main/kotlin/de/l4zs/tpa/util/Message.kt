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

package de.l4zs.tpa.util

import net.axay.kspigot.chat.KColors
import net.axay.kspigot.chat.literalText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import org.bukkit.entity.Player

object Message {

    private fun cancel(player: Player) = literalText {
        color = KColors.RED
        bold = true
        component(Component.translatable("cancel"))
        hoverEvent = HoverEvent.showText(Component.translatable("cancel.hover"))
        clickEvent = ClickEvent.runCommand("/tpcancel ${player.name}")
    }

    private fun accept(player: Player) = literalText {
        color = KColors.GREEN
        bold = true
        component(Component.translatable("accept"))
        hoverEvent = HoverEvent.showText(Component.translatable("accept.hover"))
        clickEvent = ClickEvent.runCommand("/tpaccept ${player.name}")
    }

    private fun deny(player: Player) = literalText {
        color = KColors.RED
        bold = true
        component(Component.translatable("deny"))
        hoverEvent = HoverEvent.showText(Component.translatable("deny.hover"))
        clickEvent = ClickEvent.runCommand("/tpdeny ${player.name}")
    }

    fun tpaRequestSent(player: Player) = literalText {
        color = KColors.GREEN
        component(Component.translatable("tpa.request.sent").args(player.name()))
        newLine()
        component(cancel(player))
    }

    fun tpaRequestReceived(player: Player) = literalText {
        color = KColors.GREEN
        component(Component.translatable("tpa.request.received").args(player.name()))
        newLine()
        component(accept(player))
        component(
            literalText {
                color = KColors.GRAY
                text(" | ")
            }
        )
        component(deny(player))
    }

    fun tpaHereRequestSent(player: Player) = literalText {
        color = KColors.GREEN
        component(Component.translatable("tpa.here_request.sent").args(player.name()))
        newLine()
        component(cancel(player))
    }

    fun tpaHereRequestReceived(player: Player) = literalText {
        color = KColors.GREEN
        component(Component.translatable("tpa.here_request.received").args(player.name()))
        newLine()
        component(accept(player))
        component(
            literalText {
                color = KColors.GRAY
                text(" | ")
            }
        )
        component(deny(player))
    }

    fun tpaAcceptedFrom(player: Player) = literalText {
        color = KColors.GREEN
        component(Component.translatable("tpa.request.accepted.from").args(player.name()))
    }

    fun tpaAcceptedTo(player: Player) = literalText {
        color = KColors.GREEN
        component(Component.translatable("tpa.request.accepted.to").args(player.name()))
    }

    fun tpaHereAcceptedFrom(player: Player) = literalText {
        color = KColors.GREEN
        component(Component.translatable("tpa.here_request.accepted.from").args(player.name()))
    }

    fun tpaHereAcceptedTo(player: Player) = literalText {
        color = KColors.GREEN
        component(Component.translatable("tpa.here_request.accepted.to").args(player.name()))
    }

    fun tpaDeniedFrom(player: Player) = literalText {
        color = KColors.RED
        component(Component.translatable("tpa.request.denied.from").args(player.name()))
    }

    fun tpaDeniedTo(player: Player) = literalText {
        color = KColors.RED
        component(Component.translatable("tpa.request.denied.to").args(player.name()))
    }

    fun tpaHereDeniedFrom(player: Player) = literalText {
        color = KColors.RED
        component(Component.translatable("tpa.here_request.denied.from").args(player.name()))
    }

    fun tpaHereDeniedTo(player: Player) = literalText {
        color = KColors.RED
        component(Component.translatable("tpa.here_request.denied.to").args(player.name()))
    }

    fun tpaCancelledFrom(player: Player) = literalText {
        color = KColors.RED
        component(Component.translatable("tpa.request.cancelled.from").args(player.name()))
    }

    fun tpaCancelledTo(player: Player) = literalText {
        color = KColors.RED
        component(Component.translatable("tpa.request.cancelled.to").args(player.name()))
    }

    fun tpaHereCancelledFrom(player: Player) = literalText {
        color = KColors.RED
        component(Component.translatable("tpa.here_request.cancelled.from").args(player.name()))
    }

    fun tpaHereCancelledTo(player: Player) = literalText {
        color = KColors.RED
        component(Component.translatable("tpa.here_request.cancelled.to").args(player.name()))
    }

    fun tpaRequestExpired(player: Player) = literalText {
        color = KColors.RED
        component(Component.translatable("tpa.request.expired").args(player.name()))
    }

    fun tpaHereRequestExpired(player: Player) = literalText {
        color = KColors.RED
        component(Component.translatable("tpa.here_request.expired").args(player.name()))
    }

    fun tpaAlreadyRequested(player: Player) = literalText {
        color = KColors.RED
        component(Component.translatable("tpa.request.already_requested").args(player.name()))
    }

    fun tpaHereAlreadyRequested(player: Player) = literalText {
        color = KColors.RED
        component(Component.translatable("tpa.here_request.already_requested").args(player.name()))
    }

    fun tpaToggle(enabled: Boolean) = literalText {
        color = KColors.GREEN
        component(
            Component.translatable("tpa_toggle")
                .args(
                    literalText {
                        if (enabled) {
                            color = KColors.GREEN
                            component(Component.translatable("enabled"))
                        } else {
                            color = KColors.RED
                            component(Component.translatable("disabled"))
                        }
                    }
                )
        )
        newLine()
        component(
            literalText {
                color = KColors.ORANGE
                bold = true
                component(Component.translatable("toggle"))
                hoverEvent = HoverEvent.showText(Component.translatable("toggle.hover"))
                clickEvent = ClickEvent.runCommand("/tptoggle")
            }
        )
    }

    fun tpaRequestNotFoundSender(player: Player) = literalText {
        color = KColors.RED
        component(Component.translatable("tpa.request.not_found.sender").args(player.name()))
    }

    fun tpaRequestNotFoundReceiver(player: Player) = literalText {
        color = KColors.RED
        component(Component.translatable("tpa.request.not_found.receiver").args(player.name()))
    }

    fun cannotTeleportToYourself() = literalText {
        color = KColors.RED
        component(Component.translatable("can_not_teleport_to_yourself"))
    }

    fun cannotTeleportToThatWorld() = literalText {
        color = KColors.RED
        component(Component.translatable("can_not_teleport_to_that_world"))
    }

    fun couldNotTeleport(player: Player) = literalText {
        color = KColors.RED
        component(Component.translatable("could_not_teleport").args(player.name()))
    }

    fun playerDoesNotAcceptTpaRequests(player: Player) = literalText {
        color = KColors.RED
        component(Component.translatable("player_does_not_accept_tpa_requests").args(player.name()))
    }

    fun backTeleported() = literalText {
        color = KColors.GREEN
        component(Component.translatable("teleported_back"))
    }

    fun backCannotNetherOrEnd() = literalText {
        color = KColors.RED
        component(Component.translatable("back_cannot_dimension"))
    }

    fun backNotSet() = literalText {
        color = KColors.RED
        component(Component.translatable("back_not_set"))
    }
}
