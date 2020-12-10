package net.minestom.vanilla.entityspawner;

import java.util.ArrayList;
import java.util.Random;

import net.minestom.server.event.instance.InstanceTickEvent;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.Position;
import net.minestom.server.world.DimensionType;
import net.minestom.vanilla.entity.HostileEntityProvider;

public class OverworldEntitySpawner {
	
	private InstanceContainer instance;
	
	private Random random;
	
	private int monsterCount;
	private int creatureCount;
	private int ambientCount;
	private int waterCreatureCount;
	private int waterAmbientCount;
	
	public OverworldEntitySpawner(InstanceContainer newInstance) {
		instance = newInstance;
		random = new Random();
		random.setSeed(instance.getUniqueId().getLeastSignificantBits());
		instance.addEventCallback(InstanceTickEvent.class, (event) -> {tick();});
		monsterCount = 0;
		creatureCount = 0;
		ambientCount = 0;
		waterCreatureCount = 0;
		waterAmbientCount = 0;
	}
	
	/**
	 * Spawn Cycle
		Java Edition
		
		Mobs are broadly divided into five categories: hostile, friendly, water creature (e.g., squid), water ambient (e.g., fish), and ambient (e.g., bat). 
		Hostile mobs have a spawning cycle once every game tick (1/20 of a second). Friendly and water mobs have only one spawning cycle every 400 ticks (10 seconds). 
		Because of this, where conditions permit, hostile mobs spawn frequently, but passive mobs (animals) spawn rarely. Most animals spawn within chunks when they are generated.
		
		Mobs spawn naturally within a square group of chunks centered on the player, 15×15 chunks (240×240 blocks). 
		When there are multiple players, mobs can spawn within the given distance of any of them. 
		However, hostile mobs (and some others) that move farther than 128 blocks from the nearest player despawn instantly, 
		so the mob spawning area is more-or-less limited to spheres with a radius of 128 blocks, centered at each player. (chunk radius of 8)
		In multiplayer, mob caps are shared by all players, no matter where they are.
		
		Every 24000 ticks (20 minutes) the game attempts to spawn a single wandering trader with 
		two leashed llamas within 48 blocks of a player or at a village meeting place, if no wandering trader exists in the world. 
		The trader does not spawn when the player is underground.[Java Edition only]
		
		mobCap = constant × chunks ÷ 289
		
		The constants for each group are as follows:

	    Monster = 70
	    Creature = 10
	    Ambient (bats) = 15
	    WaterCreature (squid, dolphins) = 5
	    WaterAmbient (fish) = 20
	    Misc = -1
	    
	    The "misc" category is used only by entities that are not mobs, do not spawn naturally, and/or following different spawning rules than other mobs.
	    As such the mob cap has no bearing on mobs of this category.
		In singleplayer, there are always 289 chunks in range, so the constant is always used as the global mobcap.
		In multiplayer, the global mobcap grows as more chunks are loaded.
		As chunks that are in the range of multiple players are counted only once, more chunks and higher mob caps result from the players spreading out more.
		The number of mobs is checked once per each chunk against the cap.
		If the number of mobs (dead or alive) in a category is at its cap, the entire spawning cycle for that category is skipped.
		The area checked for mobs is the same as the area used for calculating the mobcap, which is the spawning area expanded by one chunk in every direction.
		The mobcap count is separate for each dimension.
		Every chunk, the game checks the mobcap. As such, you can reach mobcap+pack size from natural spawns.
		When gamerule spectatorsgeneratechunks is false, spectators do not raise mobcap. 
		
	 */
	
	
	private void tick() {
		// Monster cycle every tick if applicable
		if (monsterCount < getMonsterMobcap()) doMonsterCycle();
		
		// Friendly cycle every 400 ticks
		if (instance.getWorldAge() % 400 == 0) {
			if (creatureCount < getCreatureMobcap()) doCreatureCycle();
			if (ambientCount < getAmbientMobcap()) doAmbientCycle();
			if (waterCreatureCount < getWaterCreatureMobcap()) doWaterCreatureCycle();
			if (waterAmbientCount < getWaterAmbientMobcap()) doWaterAmbientCycle();
		}
		
		if (instance.getWorldAge() % 20 == 0) {
			System.out.println("--------------------");
			System.out.println("Monsters: " + monsterCount + "/" + getMonsterMobcap());
			System.out.println("Creatures: " + creatureCount + "/" + getCreatureMobcap());
			System.out.println("Ambients: " + ambientCount + "/" + getAmbientMobcap());
			System.out.println("Water Creatures: " + waterCreatureCount + "/" + getWaterCreatureMobcap());
			System.out.println("Water Ambients: " + waterAmbientCount + "/" + getWaterAmbientMobcap());
		}
	}
	
	private void doMonsterCycle() {
		// Choose random chunk
		
		ArrayList<Chunk> chunkList = new ArrayList<Chunk>(); 
		
		instance.getChunks().forEach((chunk) -> {
			if (chunk.isLoaded()) {
				chunkList.add(chunk);
			}
		});
		
		Chunk chunk = chunkList.get(random.nextInt(chunkList.size()));
		
		// Get chunk X, Z
		int chunkX = chunk.getChunkX() * 16;
		int chunkZ = chunk.getChunkZ() * 16;
		
		// Find random solid block in chunk
		int posX = random.nextInt(15);
		int posY = random.nextInt(255);
		int posZ = random.nextInt(15);
		
		// try 512 times to find a valid placement
		for (int i = 0; i < 512; i++) {
			if (
				Block.fromStateId(instance.getBlockStateId(chunkX + posX, posY, chunkZ + posZ)).isSolid() && // Standing block is solid
				Block.fromStateId(instance.getBlockStateId(chunkX + posX, posY + 1, chunkZ + posZ)).isAir() && // Block above is air
				Block.fromStateId(instance.getBlockStateId(chunkX + posX, posY + 2, chunkZ + posZ)).isAir() // 2 blocks above is air
			) {
				
				System.out.println((chunkX + posX) + "|" + posY + "|" + (chunkZ + posZ));
				
				// Spawn pack
				spawnHostilePack(new Position(chunkX + posX, posY, chunkZ + posZ));
				
				return;
			}
			posX = random.nextInt(15);
			posY = random.nextInt(256);
			posZ = random.nextInt(15);
		}
	}
	
	private int getMonsterMobcap() {
		ArrayList<Chunk> chunkList = new ArrayList<Chunk>(); 
		
		instance.getChunks().forEach((chunk) -> {
			if (chunk.isLoaded()) {
				chunkList.add(chunk);
			}
		});
		
		return (int) ((70.0 * (double) chunkList.size()) / 289.0);
	}
	
	private void doCreatureCycle() {
		
	}
	
	private int getCreatureMobcap() {
		ArrayList<Chunk> chunkList = new ArrayList<Chunk>(); 
		
		instance.getChunks().forEach((chunk) -> {
			if (chunk.isLoaded()) {
				chunkList.add(chunk);
			}
		});
		return (int) ((10.0 * (double) chunkList.size()) / 289.0);
	}
	
	private void doAmbientCycle() {
		
	}
	
	private int getAmbientMobcap() {
		ArrayList<Chunk> chunkList = new ArrayList<Chunk>(); 
		
		instance.getChunks().forEach((chunk) -> {
			if (chunk.isLoaded()) {
				chunkList.add(chunk);
			}
		});
		return (int) ((15.0 * (double) chunkList.size()) / 289.0);
	}
	
	private void doWaterCreatureCycle() {
		
	}
	
	private int getWaterCreatureMobcap() {
		ArrayList<Chunk> chunkList = new ArrayList<Chunk>(); 
		
		instance.getChunks().forEach((chunk) -> {
			if (chunk.isLoaded()) {
				chunkList.add(chunk);
			}
		});
		return (int) ((5.0 * (double) chunkList.size()) / 289.0);
	}
	
	private void doWaterAmbientCycle() {
		
	}
	
	private int getWaterAmbientMobcap() {
		ArrayList<Chunk> chunkList = new ArrayList<Chunk>(); 
		
		instance.getChunks().forEach((chunk) -> {
			if (chunk.isLoaded()) {
				chunkList.add(chunk);
			}
		});
		return (int) ((20.0 * (double) chunkList.size()) / 289.0);
	}
	
	/**
	 * For each spawning cycle, attempts are made to spawn packs of mobs per each eligible chunk.
	 * An eligible chunk is determined by the same check for which chunks are random ticked.
	 * A random location in the chunk is chosen to be the center point of the pack.
	 * If the block on which a pack spawn occurs is an opaque full cube, further pack spawn attempts are canceled.
	
	The pack is spawned within a 41×1×41 (that's a 41×41 square that is one block high) area centered at the initial block.
	Mobs spawn with the lowest part of their body inside this area. For each spawn attempt, from the location of the previous attempt,
	a location up to 4 blocks away from the previous attempt is chosen at random. Thus, the spawns are heavily skewed toward the center of the pack.
	Approximately 85% of spawns are within 5 blocks of the pack center, and 99% within 10 blocks of the center.
	If the pack spawn enters a biome different from the starting biome, the rest of the pack and that spawn are canceled.
	
	All mobs within a pack are the same species.
	The species for the entire pack is chosen randomly, but based on a weight system from those eligible to spawn at the location of the first spawn attempt in the pack.
	
	The game checks on each spawn if the number of mobs that have been spawned for the pack is equal to the max spawn attempts, as well as the location's spawn potential. 
	 */
	
	private void spawnHostilePack(Position position) {
		HostileEntityProvider.INSTANCE.nextRandom(instance, position, DimensionType.OVERWORLD);
		monsterCount++;
	}
}
