package util

import BoidsPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

fun runMain(run: () -> Unit) {
    object : BukkitRunnable(){
        override fun run() {
            run()
        }
    }.runTask(BoidsPlugin.instance)
}

fun runAsync(run: () -> Unit) {
    object : BukkitRunnable(){
        override fun run() {
            run()
        }
    }.runTaskAsynchronously(BoidsPlugin.instance)
}

fun runnable(func: () -> Unit): BukkitRunnable {
    return object : BukkitRunnable(){
        override fun run() {
            func()
        }
    }
}

fun runLaterAsync(delay: Long, function: (res: BukkitTask) -> Unit) {
    var task: BukkitTask? = null

    task = object : BukkitRunnable() {
        override fun run() {
            function(task!!)
        }
    }.runTaskLaterAsynchronously(BoidsPlugin.instance, delay)
}


fun runLater(delay: Long, function: (res: BukkitTask) -> Unit) {
    var task: BukkitTask? = null

    task = object : BukkitRunnable() {
        override fun run() {
            function(task!!)
        }
    }.runTaskLater(BoidsPlugin.instance, delay)
}

fun repeat(times: Long, ticks: Long, func: (RunnableInstance) -> Unit) {
    var task: BukkitTask? = null

    val instance = RunnableInstance(times) { task?.cancel() }
    task = object : BukkitRunnable(){
        override fun run() {
            if (instance.loopNumber >= times) {
                instance.cancel()
            } else {
                instance.loopsLeft--

                if (instance.loopsLeft == 0L) instance.isLast = true
                func(instance)

                instance.loopNumber++
            }
        }
    }.runTaskTimer(BoidsPlugin.instance, 0, ticks)
}
class RunnableInstance(var loopsLeft: Long, var loopNumber: Long = 0, var isLast: Boolean = false, val cancel: () -> Unit)
