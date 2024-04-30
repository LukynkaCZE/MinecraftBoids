import commands.BoidsCommands
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.SenderMapper
import org.incendo.cloud.bukkit.CloudBukkitCapabilities
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.PaperCommandManager

class BoidsPlugin: JavaPlugin() {

    lateinit var commandManager: PaperCommandManager<CommandSender>
    companion object {
        lateinit var instance: BoidsPlugin
    }

    override fun onEnable() {
        instance = this

        // Initialize cloud command manager
        this.commandManager = PaperCommandManager(
            this,
            ExecutionCoordinator.simpleCoordinator(),
            SenderMapper.identity(),
        )
        // Initialize brigadier if supported
        if(commandManager.hasCapability(CloudBukkitCapabilities.BRIGADIER)) {
            commandManager.registerBrigadier()
        }

        // Initialize boid commands
        BoidsCommands()
    }

    override fun onDisable() {
    }
}