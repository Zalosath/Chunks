package com.jordna.chunks.chunks;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import com.jordna.chunks.main.Chunks;

public class PastedBlock
{

    public int x, y, z;
    public Material material;

    public PastedBlock(int x, int y, int z, Material material)
    {
	this.x = x;
	this.y = y;
	this.z = z;
	this.material = material;
    }

    public static class BlockQueue
    {

	private Chunks main;

	public static Deque<PastedBlock> queue = new ConcurrentLinkedDeque<>();
	public static Map<World, BlockQueue> queueMap = new ConcurrentHashMap<>();

	public void add(PastedBlock block)
	{
	    queue.add(block);
	}

	private int id = 0;

	public BlockQueue(Chunks instance, final World world)
	{
	    this.main = instance;
	    id = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.main, () ->
	    {
		PastedBlock block = null;
		boolean hasTime = true;
		long start = System.currentTimeMillis();

		while (hasTime)
		{
		    if (queue.isEmpty()) break;

		    block = queue.poll();

		    hasTime = System.currentTimeMillis() - start < main.getSettings()
			    .getInt("chunks.lag-prevention.block-place-delay"); // 10 by default
		    world.getBlockAt(block.x, block.y, block.z).setType(block.material);

		    if (block.material == Material.CHEST)
		    {
			Chest c = (Chest) world.getBlockAt(block.x, block.y, block.z).getState();
			for (ItemStack i : main.getChunkCreator().chestFill())
			    c.getInventory().addItem(i);
		    }

		}

		if (queue.isEmpty() && main.getChunkCreator().generating)
		{

		    main.getChunkCreator().finishGeneration(id);
		    queueMap.remove(world);

		}
		else
		{
		    if (!main.getChunkCreator().generating && queue.isEmpty())
		    {

			main.getChunkCreator().finishAbandonment(id);
			queueMap.remove(world);

		    }
		}

	    }, 1, 1);
	}

	public static BlockQueue getQueue(Chunks instance, World w)
	{
	    if (queueMap != null)
	    {
		if (!queueMap.containsKey(w))
		{
		    BlockQueue blockQueue = new BlockQueue(instance, w);
		    queueMap.put(w, blockQueue);

		    return blockQueue;
		}
	    }
	    return queueMap.get(w);
	}
    }
}
