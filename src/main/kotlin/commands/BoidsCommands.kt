package commands

import BoidManager
import BoidsPlugin
import org.bukkit.entity.Player
import org.incendo.cloud.parser.standard.IntegerParser.integerParser
import org.incendo.cloud.parser.standard.StringParser.stringParser
import org.incendo.cloud.suggestion.SuggestionProvider
import util.send

class BoidsCommands {

    val commandManager = BoidsPlugin.instance.commandManager
    init {
        val command = commandManager.commandBuilder("boids")
        commandManager.command(command
            .required("action", stringParser(), SuggestionProvider.suggestingStrings("spawn", "tick", "remove"))
            .optional("amount", integerParser(), SuggestionProvider.suggestingStrings("<amount>"))
            .optional("spread", integerParser(), SuggestionProvider.suggestingStrings("<spread>"))
            .handler {
                val action = it.get<String>("action")

                val player = it.sender() as Player
                when (action) {
                    "spawn" -> {
                        val amount = it.getOrDefault("amount", 5)
                        val spread = it.getOrDefault("spread", 1)
                        BoidManager.spawnBoids(player.location, amount, spread)
                        player.send("<green>Spawned <yellow>$amount <green>boids with spread of <aqua>$spread blocks")
                    }
                    "remove" -> {
                        BoidManager.removeBoids()
                    }
                    "tick" -> {
                        val tick = BoidManager.tick

                        when(!tick) {
                            true -> { player.send("<green>Ticking of boids resumed"); BoidManager.tick = true; BoidManager.tickBoids()}
                            else -> { player.send("<red>Ticking of boids paused"); BoidManager.tick = false }
                        }
                    }
                }
            }
        )
    }
}