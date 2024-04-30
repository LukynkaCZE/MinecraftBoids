# Minecraft Boids
This is implementation of Boids in PaperMC Minecraft plugin

## How to run
Run task `runMojangMappedServer` in IntelliJ (or any other IDE with kotlin support)
This should start a minecraft server on default port `25565` (Configurable in file `server.properties` in the `/run` folder)

Once you join the server, you can run few commands:
- `/boids spawn <amount> <distance between boids>` - This will spawn specified amount of boids with distance between them at your location
- `/boids tick` - Toggles if boids tick or not (think of it as being processed, if its off they will be frozen in time and not do any calculations)
- `/boids remove` - Removes all boids

---

**`⛔` Warning:**
This is in no way meant to be run on production servers. Its just fun little thing I decided to make to practice vector math. It is **VERY** inefficient. I recommend not spawning more than 200 boids at a time and I recommend spawning them in some kinda of closed area. If you spawn them without any enclosure, they will just fly away lol

`🐟` Thanks to [Sebastial Lague](https://www.youtube.com/@SebastianLague) for the inspiration, check out his video on boids here: https://youtu.be/bqtqltqcQhw

`☕` If you like my work, support me on ko-fi: https://ko-fi.com/lukynkacze