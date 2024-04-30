package util

import org.bukkit.Location
import org.bukkit.entity.Entity
import kotlin.random.Random

fun randomLocationInRadius(location: Location, radius: Double): Location {
    val randomX = Random.nextDouble(radius * -1, radius)
    val randomY = Random.nextDouble(radius * -1, radius)
    val randomZ = Random.nextDouble(radius * -1, radius)

    return Location(location.world, location.x + randomX, location.y + randomY, location.z + randomZ)
}

fun Entity.getEntitiesInRadius(radius: Double): MutableList<Entity> {
    return this.getNearbyEntities(radius, radius, radius)
}