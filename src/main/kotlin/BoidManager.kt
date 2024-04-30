import org.bukkit.Location
import util.randomLocationInRadius

object BoidManager {

    val boids = mutableListOf<Boid>()
    var group = 0

    fun spawnBoids(location: Location, amount: Int, spread: Int) {
        for (i in 0..amount) boids.add(Boid(randomLocationInRadius(location, spread.toDouble()), group.toString()))
        group++
    }

    var tick = false

    fun tickBoids() {
        util.repeat(4, 1) { runnable ->

            if(!tick) {
                runnable.loopsLeft = 0
                runnable.cancel
            }

            boids.forEach { it.tick() }
            boids.forEach { it.tick() }

            // I crashed the server twice when writing this oops
            if(runnable.isLast) tickBoids()
        }
    }

    fun removeBoids() {
        boids.forEach { it.remove() }
        boids.clear()
    }
}