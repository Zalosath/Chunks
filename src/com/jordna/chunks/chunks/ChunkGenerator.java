package com.jordna.chunks.chunks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.jordna.chunks.main.Chunks;

public class ChunkGenerator
{

    private Chunks main;

    public ChunkGenerator(Chunks ins)
    {
	this.main = ins;
    }

    private Random random = new Random();

    private ChunkInformation c_Info;

    private List<ItemStack> starterItems = new ArrayList<ItemStack>();

    private List<ItemStack> loot_table = new ArrayList<ItemStack>();

    private List<Item> items = new ArrayList<Item>();

    private HashMap<Integer, HashMap<Integer, List<Item>>> blockChances = new HashMap<Integer, HashMap<Integer, List<Item>>>();

    public boolean generating = true;

    private ItemStack getItemStack(FileConfiguration config, String path, String s)
    {
	ItemStack i = new ItemStack(Material.matchMaterial(s), config.getInt(path + s + ".amount"));
	ItemMeta im = i.getItemMeta();
	im.setDisplayName(config.getString(path + s + ".display-name"));
	i.setItemMeta(im);
	return i;

    }

    public void preInit()
    {
	this.c_Info = new ChunkInformation(null, null, 1, 1, 1);

	if (main.getStarter().isConfigurationSection("chunks.start-chest.items"))
	{
	    for (String s : main.getStarter().getConfigurationSection("chunks.start-chest.items").getKeys(false))
	    {
		starterItems.add(getItemStack(main.getStarter(), "chunks.start-chest.items.", s));
	    }
	}

	if (main.getChunk().isConfigurationSection("chunks.block-chances"))
	{
	    int count = 0;
	    for (String s : main.getChunk().getConfigurationSection("chunks.block-chances").getKeys(false))
	    {
		Material m = Material.matchMaterial(s);
		if (m != null)
		{
		    float chance = Float.parseFloat(main.getChunk().getString("chunks.block-chances." + s + ".chance"));
		    int max = main.getChunk().getInt("chunks.block-chances." + s + ".maximum-mining-level");
		    int min = main.getChunk().getInt("chunks.block-chances." + s + ".minimum-mining-level");

		    items.add(new Item(m, chance, max, min));
		}

		count++;

		if (count >= main.getChunk().getConfigurationSection("chunks.block-chances").getKeys(false).size())
		{
		    for (int d = 0; d < 11; d++)
		    {
			HashMap<Integer, List<Item>> hash = new HashMap<Integer, List<Item>>();
			for (int y = 0; y < 70; y++)
			{

			    List<Item> itemLevels = new ArrayList<Item>();
			    for (Item i : this.items)
			    {
				if (y >= i.lowestLevel && y <= i.highestLevel)
				{
				    Item i2 = new Item(i);

				    if (i2.material.name().toLowerCase().contains("ore"))
					i2.probability = i2.probability / d;

				    itemLevels.add(i2);
				}
			    }

			    float sum = 0;

			    for (Item i : itemLevels)
				sum += i.probability;

			    for (Item i : itemLevels)
				i.probability /= sum;

			    hash.put(y, itemLevels);
			}
			this.blockChances.put(d, hash);
		    }
		}
	    }
	}

    }

    public void initialize(World world, Player player, int xBorder, int zBorder, int difficulty)
    {
	this.c_Info = new ChunkInformation(world, player, xBorder, zBorder, difficulty);

	items.clear();

	if (main.getLoot().isConfigurationSection("chunks.loot-table.items"))
	{
	    for (String s : main.getLoot().getConfigurationSection("chunks.loot-table.items").getKeys(false))
	    {
		this.loot_table.add(getItemStack(main.getLoot(), "chunks.loot-table.items.", s));
	    }
	}

    }

    public Location findEmptyArea(Player player, World world, int iter)
    {

	if (iter > 10000000)
	{
	    player.sendMessage(
		    ChatColor.RED + "[CHUNKS] 10,000,000 chunk limit reached. Please contact a server administrator.");
	    return null;
	}

	int chunkSize = main.getSettings().getInt("chunks.default-size-in-chunks") * 32;

	int n = main.getSettings().getInt("chunk-count-do-not-change");

	int step_count = 1;
	int step_limit = 2;
	int adder = 1;
	int x = 0, z = 0;

	for (int c = 2; c != n + 1; c++, step_count++)
	{
	    if (step_count <= .5 * step_limit)
		x += adder;
	    else if (step_count <= step_limit) z += adder;
	    if (step_count == step_limit)
	    {
		adder *= -1;
		step_limit += 2;
		step_count = 0;
	    }
	}

	// we start with simple numbers,
	// e.g 0.0 0.0, 1.0 0.0, 1.0 1.0, 0.0 1.0
	// we convert these numbers into coordinates by multiplying by a set spacing
	// value (256 blocks)
	// e.g 0.0 0.0, 256.0 0.0, 256.0 256.0, 0.0 256.0
	// we then find the coordinates of the centre of this chunk by adding 8 to X and
	// Z
	// e.g 8.0 8.0, 264.0 8.0, 264.0 264.0, 8.0 264.0
	// send these coordinates to getChunkIn and any chunk within 256 blocks of the
	// centre block will be returned
	// e.g if chunk exists at 264.0 264.0, and new chunk is being created at 8.0
	// 520.0
	// e.g difference would be 264.0 - 8.0 = 256.0 AND 264.0 - 520.0 = 256

	Location location = new Location(world, x * chunkSize, 67, z * chunkSize);
	Location centre = new Location(world, location.getBlockX() + 8.0, 67, location.getBlockZ() + 8.0);

	Chunk chunkBoundary = main.getChunkManager().getOverlapsChunk(centre);

	if (chunkBoundary != null)
	{
	    System.out.println("[CHUNKS DEBUG] Chunk (apparently) infringes within bounds. Existing chunk, X: "
		    + chunkBoundary.getCentre().getBlockX() + " Z: " + chunkBoundary.getCentre().getBlockZ());
	    System.out.println("[CHUNKS DEBUG] Increasing chunk count to compensate");
	    this.main.getSettings().set("chunk-count-do-not-change", n + 1);
	    this.main.saveSettings();
	    return findEmptyArea(player, world, iter++);
	}

	return location;
    }

    public void addToQueue(Material material, int x, int y, int z)
    {
	PastedBlock.BlockQueue.getQueue(main, this.c_Info.getWorld()).add(new PastedBlock(x, y, z, material));
    }

    public void deleteChunk(Chunk chunk, Player player)
    {
	int defaultX = main.getChunk().getInt("chunks.default-location.X");
	int defaultY = main.getChunk().getInt("chunks.default-location.Y");
	int defaultZ = main.getChunk().getInt("chunks.default-location.Z");
	World w = Bukkit.getWorld(main.getChunk().getString("chunks.default-location.WORLD"));

	if (w == null)
	{
	    player.sendMessage(ChatColor.RED
		    + "[CHUNKS] Failed to find spawn location, aborting chunk deletion. Please contact a server administrator, they may need to configure the default location in the config.");
	    return;
	}
	else
	{
	    player.teleport(new Location(w, defaultX, defaultY, defaultZ));
	}

	generating = false;

	int chunkAllowance = main.getSettings().getInt("chunks.default-size-in-chunks") * 16;

	int centreX = chunk.getCentre().getBlockX();
	int centreZ = chunk.getCentre().getBlockZ();

	this.c_Info.setBorderX(centreX - 8);
	this.c_Info.setBorderZ(centreZ - 8);
	this.c_Info.setPlayer(player);
	this.c_Info.setWorld(chunk.getHome().getWorld());

	for (int x = centreX - chunkAllowance; x < centreX + chunkAllowance; x++)
	{
	    for (int z = centreZ - chunkAllowance; z < centreZ + chunkAllowance; z++)
	    {
		for (int y = 0; y < 257; y++)
		{
		    Block block = chunk.getHome().getWorld()
			    .getBlockAt(new Location(chunk.getHome().getWorld(), x, y, z));
		    if (block.getType() != Material.AIR)
		    {

			if (block.getType() == Material.CHEST)
			{
			    Chest c = (Chest) block.getState();

			    c.getInventory().clear();
			}

			this.addToQueue(Material.AIR, x, y, z);
		    }
		}
	    }
	}

	main.getChunkManager().removeChunk(chunk);

	main.updateChunkCounter(-1);
    }

    public void finishAbandonment(int id)
    {
	this.c_Info.getPlayer().sendMessage(ChatColor.RED + "[CHUNKS] Your chunk has been permanently removed.");

	main.getChunks().set("chunks." + this.c_Info.getPlayer().getUniqueId().toString(), null);
	main.saveChunks();

	Bukkit.getScheduler().cancelTask(id);
    }

    public List<ItemStack> chestFill()
    {
	List<ItemStack> table = new ArrayList<ItemStack>();
	for (int i = 0; i < (4 - this.c_Info.getDifficulty()); i++)
	    if (random.nextInt(2 * this.c_Info.getDifficulty()) == 1)
		table.add(this.loot_table.get(random.nextInt(this.loot_table.size())));

	return table;
    }

    private void makeCircle(Location loc, Integer radius, Material m)
    {
	for (int y = 0; y < 4; y++)
	{
	    int radiusSquared = (radius * radius) + y;

	    for (int x = -radius; x <= radius; x++)
	    {
		for (int z = -radius; z <= radius; z++)
		{
		    if ((x * x) + (z * z) <= radiusSquared)
		    {
			loc.getWorld().getBlockAt(new Location(loc.getWorld(), x + loc.getBlockX(), loc.getBlockY() + y,
				z + loc.getBlockZ())).setType(m);
			;
		    }
		}
	    }
	}
    }

    private void placeStarterChest(World world, int xBorder, int zBorder)
    {
	Location loc = new Location(world, xBorder + 8, 66, zBorder + 7);

	Block b = world.getBlockAt(loc);
	b.setType(Material.CHEST);

	Chest c = (Chest) b.getState();

	for (ItemStack i : starterItems)
	{
	    c.getInventory().addItem(i);
	}
    }

    private Material returnRandomBlock(int y)
    {

	double p = Math.random();
	double cumulativeProbability = 0.0;
	List<Item> included = this.blockChances.get(this.c_Info.getDifficulty()).get(y);
	for (Item item : included)
	{
	    cumulativeProbability += item.probability;
	    if (p <= cumulativeProbability)
	    {
		return item.material;
	    }
	}

	return Material.STONE;

    }

    private void generatePool(World world, Material material, int xBorder, int zBorder, int yMod)
    {
	int yLevel = random.nextInt(10) + 5;

	makeCircle(new Location(world, xBorder + random.nextInt(4), yLevel + yMod, zBorder + random.nextInt(6)), 2,
		material);

    }

    public boolean generateChunk(Player player, World world, int difficulty)
    {

	if (!PastedBlock.BlockQueue.queue.isEmpty())
	{
	    player.sendMessage(ChatColor.RED + "[CHUNKS] Please try again in approximitely 10 seconds");
	    return false;
	}

	if (main.getChunkManager().hasChunk(player))
	{
	    player.sendMessage(
		    ChatColor.RED + "[CHUNKS] You are already a member of a chunk, please abandon/leave it first");
	    return false;
	}

	generating = true;

	Location loc = this.findEmptyArea(player, world, 0);
	if (loc == null)
	{
	    generating = false;
	    return false;
	}

	this.initialize(world, player, loc.getBlockX(), loc.getBlockZ(), difficulty);

	for (int x = this.c_Info.getBorderX(); x < this.c_Info.getBorderX() + 16; x++)
	{
	    for (int z = this.c_Info.getBorderZ(); z < this.c_Info.getBorderZ() + 16; z++)
	    {
		for (int y = 0; y < 66; y++)
		{
		    Material matToPlace = Material.STONE;

		    if (y == 65)
		    {
			matToPlace = Material.GRASS_BLOCK;
		    }
		    else if (y < 65 && y > 62)
		    {
			matToPlace = Material.DIRT;
		    }
		    else if (y > 1 && y < 62)
		    {
			matToPlace = this.returnRandomBlock(y);
		    }
		    else if (y == 0)
		    {
			matToPlace = Material.BEDROCK;
		    }
		    this.addToQueue(matToPlace, x, y, z);
		}
	    }
	}

	return true;

    }

    public void finishGeneration(int id)
    {

	if (this.c_Info.getWorld().getBlockAt(
		new Location(this.c_Info.getWorld(), this.c_Info.getBorderX() + 8, 66, this.c_Info.getBorderZ() + 8))
		.getType() == Material.AIR)
	{
	    this.c_Info.getWorld().generateTree(new Location(this.c_Info.getWorld(), this.c_Info.getBorderX() + 8, 66,
		    this.c_Info.getBorderZ() + 8), TreeType.BIG_TREE);
	}

	this.generatePool(this.c_Info.getWorld(), Material.WATER, this.c_Info.getBorderX(), this.c_Info.getBorderZ(),
		25);
	this.generatePool(this.c_Info.getWorld(), Material.LAVA, this.c_Info.getBorderX() + 14,
		this.c_Info.getBorderZ() + 6, 0);

	this.placeStarterChest(this.c_Info.getWorld(), this.c_Info.getBorderX(), this.c_Info.getBorderZ());

	this.c_Info.getPlayer().teleport(
		new Location(this.c_Info.getWorld(), this.c_Info.getBorderX() + 8, 67, this.c_Info.getBorderZ() + 1));
	this.c_Info.getPlayer().getLocation().setYaw(0f);
	this.c_Info.getPlayer().getLocation().setPitch(0f);

	main.getChunks().set("chunks." + this.c_Info.getPlayer().getUniqueId() + ".X",
		this.c_Info.getPlayer().getLocation().getBlockX());
	main.getChunks().set("chunks." + this.c_Info.getPlayer().getUniqueId() + ".Y",
		this.c_Info.getPlayer().getLocation().getBlockY());
	main.getChunks().set("chunks." + this.c_Info.getPlayer().getUniqueId() + ".Z",
		this.c_Info.getPlayer().getLocation().getBlockZ());
	main.getChunks().set("chunks." + this.c_Info.getPlayer().getUniqueId() + ".WORLD",
		this.c_Info.getPlayer().getLocation().getWorld().getName());

	main.getChunks().set("chunks." + this.c_Info.getPlayer().getUniqueId() + ".centre.X",
		this.c_Info.getBorderX() + 8);
	main.getChunks().set("chunks." + this.c_Info.getPlayer().getUniqueId() + ".centre.Z",
		this.c_Info.getBorderZ() + 8);

	List<String> members = new ArrayList<String>();
	members.add(this.c_Info.getPlayer().getUniqueId().toString());

	main.getChunks().set("chunks." + this.c_Info.getPlayer().getUniqueId() + ".members", members);

	List<String> trusted = new ArrayList<String>();
	trusted.add(this.c_Info.getPlayer().getUniqueId().toString());

	main.getChunks().set("chunks." + this.c_Info.getPlayer().getUniqueId() + ".trusted", trusted);

	main.saveChunks();

	main.getChunkManager().addChunk(this.c_Info.getPlayer().getUniqueId().toString());

	this.c_Info.getPlayer().sendMessage(ChatColor.BLUE + "[CHUNKS] Welcome to your new chunk.");

	if (this.main.getSettings().getBoolean("chunks.chunk.clear-inventory-on-create"))
	    this.c_Info.getPlayer().getInventory().clear();

	this.main.updateChunkCounter(1);
	this.main.getSettings().set("chunk-count-do-not-change",
		this.main.getSettings().getInt("chunk-count-do-not-change") + 1);
	this.main.saveSettings();

	Bukkit.getScheduler().cancelTask(id);
    }

}
