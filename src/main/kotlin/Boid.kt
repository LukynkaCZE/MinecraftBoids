import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import util.getEntitiesInRadius
import util.miniMessage
import kotlin.math.atan2
import kotlin.math.sqrt
import kotlin.random.Random

class Boid(var location: Location, val group: String) {

    lateinit var entity: LivingEntity

    init {
        spawn()
    }

    fun spawn() {
        entity = location.world.spawnEntity(location, EntityType.TROPICAL_FISH) as LivingEntity

        //Make sure they can't die and don't make noises
        entity.isInvulnerable = true
        entity.isSilent = true

        // Make sure they don't fall. lol
        entity.setGravity(false)

        // They don't need to be sentient
        entity.setAI(false)

        // Set their name so only the entities with same name can be affected by the boid algorithm
        entity.customName(miniMessage.deserialize(group))
        entity.isCustomNameVisible = false

        // Randomize their direction on spawn
        val rLoc = entity.location
        rLoc.yaw = Random.nextDouble(0.0, 360.0).toFloat()
        entity.teleport(rLoc)
    }

    fun tick() {
        val speedModifier = Random.nextDouble(1.2, 4.0)
        val avoidVector = avoidEachOtherAndBlocks()
        val alignVector = alignWithSameName()
        val cohesionVector = cohesionWithSameName()

        // Combine the vectors
        val combinedVector = avoidVector.add(alignVector).add(cohesionVector)

        // If the combined vector is not zero, calculate yaw and pitch
        if (!combinedVector.isZero) {
            val direction = combinedVector.clone().normalize()
            val horizontalDistance = sqrt(direction.x * direction.x + direction.z * direction.z)
            val yaw = Math.toDegrees(atan2(-direction.x, direction.z)).toFloat()
            val pitch = Math.toDegrees(atan2(-direction.y, horizontalDistance)).toFloat()

            // Set entity's yaw and pitch
            val finalLocation = entity.location.clone()
            finalLocation.yaw = yaw
            finalLocation.pitch = pitch

            // Move entity in the direction multiplied by speed modifier (just to make each entity little more diverse)
            entity.teleport(finalLocation.add(combinedVector.multiply(speedModifier)))
        } else {
            // If the combined vectr is zero, we don't change the rotation of the entity
            entity.teleport(entity.location.add(combinedVector.multiply(speedModifier)))
        }
    }

    fun avoidEachOtherAndBlocks(): Vector {
        // How far should the entity stay away from other entities
        val entityAvoidDistance = Random.nextDouble(0.0, 1.5)

        // How far should the entity stay away from blocks
        val blockAvoidDistance = 1

        // the strength of the avoidance behavior
        val avoidanceFactor = Random.nextDouble(0.01, 0.1)
        val avoidVector = Vector(0, 0, 0)

        val nearbyEntities = entity.getEntitiesInRadius(5.0)
        val nearbyBlocks = getNearbyBlocks(5.0)

        for (nearbyEntity in nearbyEntities) {
            if (nearbyEntity.customName() == entity.customName()) {
                val distance = entity.location.distance(nearbyEntity.location)
                if (distance < entityAvoidDistance) {
                    val awayDirection = entity.location.toVector().subtract(nearbyEntity.location.toVector()).normalize()
                    avoidVector.add(awayDirection.multiply(avoidanceFactor))
                }
            }
        }

        for (blockLoc in nearbyBlocks) {
            val distance = entity.location.distance(blockLoc)
            if (distance < blockAvoidDistance) {
                val awayDirection = entity.location.toVector().subtract(blockLoc.toVector()).normalize()
                avoidVector.add(awayDirection.multiply(avoidanceFactor))
            }
        }

        return avoidVector
    }

    fun alignWithSameName(): Vector {

        // distance
        val alignDistance = 5.0

        // the strength of the alignment behavior (0.05 seems to be good)
        val alignFactor = 0.05
        val alignVector = Vector(0, 0, 0)

        val nearbyEntities = entity.getEntitiesInRadius(alignDistance)
        if (nearbyEntities.isNotEmpty()) {
            for (nearbyEntity in nearbyEntities) {
                if (nearbyEntity.customName() == entity.customName()) {
                    alignVector.add(nearbyEntity.location.direction.normalize())
                }
            }
            alignVector.divide(Vector(nearbyEntities.size.toDouble(), nearbyEntities.size.toDouble(), nearbyEntities.size.toDouble()))
            alignVector.multiply(alignFactor)
        }

        return alignVector
    }

    fun cohesionWithSameName(): Vector {
        // Distance
        val cohesionDistance = 5.0
        // Strength of cohesion
        val cohesionFactor = 0.01
        val cohesionVector = Vector(0, 0, 0)

        val nearbyEntities = entity.getEntitiesInRadius(cohesionDistance)
        if (nearbyEntities.isNotEmpty()) {
            val center = Vector(0, 0, 0)
            var count = 0
            for (nearbyEntity in nearbyEntities) {
                if (nearbyEntity.customName() == entity.customName()) {
                    center.add(nearbyEntity.location.toVector())
                    count++
                }
            }
            if (count > 0) {
                center.divide(Vector(count.toDouble(), count.toDouble(), count.toDouble()))
                val directionToCenter = center.subtract(entity.location.toVector()).normalize()
                cohesionVector.add(directionToCenter.multiply(cohesionFactor))
            }
        }

        return cohesionVector
    }

    // ugly. but im lazy to come up with something better
    fun getNearbyBlocks(radius: Double): List<Location> {
        val nearbyBlocks = mutableListOf<Location>()
        val blockLocation = entity.location.toBlockLocation()
        for (x in -radius.toInt()..radius.toInt()) {
            for (y in -radius.toInt()..radius.toInt()) {
                for (z in -radius.toInt()..radius.toInt()) {
                    val loc = blockLocation.clone().add(x.toDouble(), y.toDouble(), z.toDouble())
                    if (loc.block.type.isSolid) nearbyBlocks.add(loc)
                }
            }
        }
        return nearbyBlocks
    }

    fun remove() {
        entity.remove()
    }

}