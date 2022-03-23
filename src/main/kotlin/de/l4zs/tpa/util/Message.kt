package de.l4zs.tpa.util

import net.axay.kspigot.chat.KColors
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player

object Message {

    fun tpaRequestSent(to: Player) = Component.translatable("tpa_request.sent")
        .args(Component.text(to.name))
        .color(KColors.GREEN)
        .append(Component.newline())
        .append(
            Component.translatable("cancel")
                .decorate(TextDecoration.BOLD)
                .color(KColors.RED)
                .hoverEvent(HoverEvent.showText(Component.translatable("cancel.hover")))
                .clickEvent(ClickEvent.runCommand("/tpcancel ${to.name}"))
        )

    fun tpaRequestReceived(from: Player) = Component.translatable("tpa_request.received")
        .args(Component.text(from.name))
        .color(KColors.GREEN)
        .append(Component.newline())
        .append(
            Component.translatable("accept")
                .decorate(TextDecoration.BOLD)
                .color(KColors.GREEN)
                .hoverEvent(HoverEvent.showText(Component.translatable("accept.hover")))
                .clickEvent(ClickEvent.runCommand("/tpaccept ${from.name}"))
        )
        .append(
            Component.text(" | ")
                .color(KColors.GRAY)
        )
        .append(
            Component.translatable("deny")
                .decorate(TextDecoration.BOLD)
                .color(KColors.RED)
                .hoverEvent(HoverEvent.showText(Component.translatable("deny.hover")))
                .clickEvent(ClickEvent.runCommand("/tpdeny ${from.name}"))
        )

    fun tpaHereRequestSent(to: Player) = Component.translatable("tpa_here_request.sent")
        .args(Component.text(to.name))
        .color(KColors.GREEN)
        .append(Component.newline())
        .append(
            Component.translatable("cancel")
                .decorate(TextDecoration.BOLD)
                .color(KColors.RED)
                .hoverEvent(HoverEvent.showText(Component.translatable("cancel.hover")))
                .clickEvent(ClickEvent.runCommand("/tpcancel ${to.name}"))
        )

    fun tpaHereRequestReceived(from: Player) = Component.translatable("tpa_here_request.received")
        .args(Component.text(from.name))
        .color(KColors.GREEN)
        .append(Component.newline())
        .append(
            Component.translatable("accept")
                .decorate(TextDecoration.BOLD)
                .color(KColors.GREEN)
                .hoverEvent(HoverEvent.showText(Component.translatable("accept.hover")))
                .clickEvent(ClickEvent.runCommand("/tpaccept ${from.name}"))
        )
        .append(
            Component.text(" | ")
                .color(KColors.GRAY)
        )
        .append(
            Component.translatable("deny")
                .decorate(TextDecoration.BOLD)
                .color(KColors.RED)
                .hoverEvent(HoverEvent.showText(Component.translatable("deny.hover")))
                .clickEvent(ClickEvent.runCommand("/tpdeny ${from.name}"))
        )

    fun tpaAcceptedFrom(to: Player) = Component.translatable("tpa_request.accepted_from")
        .args(Component.text(to.name))
        .color(KColors.GREEN)

    fun tpaAcceptedTo(from: Player) = Component.translatable("tpa_request.accepted_to")
        .args(Component.text(from.name))
        .color(KColors.GREEN)

    fun tpaHereAcceptedFrom(to: Player) = Component.translatable("tpa_here_request.accepted_from")
        .args(Component.text(to.name))
        .color(KColors.GREEN)

    fun tpaHereAcceptedTo(from: Player) = Component.translatable("tpa_here_request.accepted_to")
        .args(Component.text(from.name))
        .color(KColors.GREEN)

    fun tpaDeniedFrom(to: Player) = Component.translatable("tpa_request.denied_from")
        .args(Component.text(to.name))
        .color(KColors.RED)

    fun tpaDeniedTo(from: Player) = Component.translatable("tpa_request.denied_to")
        .args(Component.text(from.name))
        .color(KColors.RED)

    fun tpaHereDeniedFrom(to: Player) = Component.translatable("tpa_here_request.denied_from")
        .args(Component.text(to.name))
        .color(KColors.RED)

    fun tpaHereDeniedTo(from: Player) = Component.translatable("tpa_here_request.denied_to")
        .args(Component.text(from.name))
        .color(KColors.RED)

    fun tpaCancelledFrom(to: Player) = Component.translatable("tpa_request.cancelled_from")
        .args(Component.text(to.name))
        .color(KColors.RED)

    fun tpaCancelledTo(from: Player) = Component.translatable("tpa_request.cancelled_to")
        .args(Component.text(from.name))
        .color(KColors.RED)

    fun tpaHereCancelledFrom(to: Player) = Component.translatable("tpa_here_request.cancelled_from")
        .args(Component.text(to.name))
        .color(KColors.RED)

    fun tpaHereCancelledTo(from: Player) = Component.translatable("tpa_here_request.cancelled_to")
        .args(Component.text(from.name))
        .color(KColors.RED)

    fun tpaRequestExpired(to: Player) = Component.translatable("tpa_request.expired")
        .args(Component.text(to.name))
        .color(KColors.RED)

    fun tpaHereRequestExpired(to: Player) = Component.translatable("tpa_here_request.expired")
        .args(Component.text(to.name))
        .color(KColors.RED)

    fun tpaAlreadyRequested(to: Player) = Component.translatable("tpa_request.already_requested")
        .args(Component.text(to.name))
        .color(KColors.RED)

    fun tpaHereAlreadyRequested(to: Player) = Component.translatable("tpa_here_request.already_requested")
        .args(Component.text(to.name))
        .color(KColors.RED)

    fun tpaToggle(enabled: Boolean) = Component.translatable("tpa_toggle")
        .args((if (enabled) Component.translatable("enabled") else Component.translatable("disabled")).color(if (enabled) KColors.GREEN else KColors.RED))
        .color(KColors.GREEN)
        .append(Component.newline())
        .append(
            Component.translatable("toggle")
                .decorate(TextDecoration.BOLD)
                .color(KColors.ORANGE)
                .hoverEvent(HoverEvent.showText(Component.translatable("toggle.hover")))
                .clickEvent(ClickEvent.runCommand("/tptoggle"))
        )

    fun tpaRequestNotFoundSender(player: Player) = Component.translatable("tpa_request.not_found_sender")
        .args(Component.text(player.name))
        .color(KColors.RED)

    fun tpaRequestNotFoundReceiver(player: Player) = Component.translatable("tpa_request.not_found_receiver")
        .args(Component.text(player.name))
        .color(KColors.RED)

    fun cannotTeleportToYourself() = Component.translatable("can_not_teleport_to_yourself")
        .color(KColors.RED)

    fun cannotTeleportToDifferentDimension() = Component.translatable("can_not_teleport_to_different_dimension")
        .color(KColors.RED)

    fun couldNotTeleport(player: Player) = Component.translatable("could_not_teleport")
        .args(Component.text(player.name))
        .color(KColors.RED)

    fun playerDoesNotAcceptTpaRequests(player: Player) = Component.translatable("player_does_not_accept_tpa_requests")
        .args(Component.text(player.name))
        .color(KColors.RED)

    fun backTeleported() = Component.translatable("back_teleported")
        .color(KColors.GREEN)

    fun backCannotNetherOrEnd() = Component.translatable("back_cannot_nether_or_end")
        .color(KColors.RED)

    fun backNotSet() = Component.translatable("back_not_set")
        .color(KColors.RED)
}
