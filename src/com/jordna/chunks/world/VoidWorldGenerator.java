package com.jordna.chunks.world;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;

public class VoidWorldGenerator extends ChunkGenerator
{

	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome)
	{
		ChunkData chunk = createChunkData(world);
		for (int x = 0; x < 16; x++)
		{
			for (int z = 0; z < 16; z++)
			{
				for (int y = 0; y < 50; y++)
				{
					chunk.setBlock(x, y, z, Material.AIR);
				}
			}
		}
		return chunk;
	}

	public void generateVoidWorld()
	{
		WorldCreator creator = new WorldCreator("chunksworld");
		
		creator.generator(this);
		
		World world = creator.createWorld();
		world.getBlockAt(new Location(world, 0d, 64d, 0d)).setType(Material.BEDROCK);
	}
	
}
