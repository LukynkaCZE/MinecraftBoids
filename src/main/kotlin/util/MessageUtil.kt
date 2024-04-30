package util

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player

val miniMessage = MiniMessage.miniMessage()

fun Player.send(message: String) {
    this.sendMessage(miniMessage.deserialize(message))
}