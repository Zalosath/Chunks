package com.jordna.chunks.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.jordna.chunks.chunks.ChunkGenerator;
import com.jordna.chunks.chunks.ChunkManager;
import com.jordna.chunks.commands.ChunkCreatorExecutor;
import com.jordna.chunks.events.PlayerJoin;
import com.jordna.chunks.events.SignListeners;
import com.jordna.chunks.inventory.DifficultyInventory;
import com.jordna.chunks.inventory.MenuInventory;
import com.jordna.chunks.inventory.SettingsInventory;
import com.jordna.chunks.metrics.Metrics;
import com.jordna.chunks.protection.PlayerProtectionEvents;
import com.jordna.chunks.protection.ProtectedAreaManager;
import com.jordna.chunks.world.VoidWorldGenerator;

public class Chunks extends JavaPlugin 
{

	private ChunkGenerator chunkGenerator;
	
	private DifficultyInventory diffInventory;
	private MenuInventory menuInventory;
	private SettingsInventory settingsInventory;
	
	private ChunkManager chunkManager;
	private ProtectedAreaManager protectionManager;
	
	private File chunkFile, chunksFile, startChestFile, lootTableFile, settingsFile, uuidFile, messagesFile;
	private FileConfiguration chunk, chunks, startChest, lootTable, settings, uuid, messages;
	
	private Metrics metrics;
	
	private int chunkCounter = 0;
	
	@Override
	public void onEnable()
	{
		
		metrics = new Metrics(this);
		
		this.initFiles();
		
		if (this.getChunk().getInt("chunks.difficulty-multipliers.easy") > 10)
		{
			this.getChunk().set("chunks.difficulty-multipliers.easy", 10);
		}
		
		if (this.getChunk().getInt("chunks.difficulty-multipliers.medium") > 10)
		{
			this.getChunk().set("chunks.difficulty-multipliers.medium", 10);
		}
		
		if (this.getChunk().getInt("chunks.difficulty-multipliers.hard") > 10)
		{
			this.getChunk().set("chunks.difficulty-multipliers.hard", 10);
		}
		
		this.saveChunk();
		
		this.chunkGenerator = new ChunkGenerator(this);
		this.chunkGenerator.preInit();
		
		this.diffInventory = new DifficultyInventory(this);
		this.diffInventory.initializeInventory();
		
		this.menuInventory = new MenuInventory(this);
		this.menuInventory.initializeInventory();
		
		this.settingsInventory = new SettingsInventory(this);
		this.settingsInventory.initializeInventory();
		
		this.chunkManager = new ChunkManager(this);
		
		this.protectionManager = new ProtectedAreaManager(this);
		
		if (Bukkit.getWorld("chunksworld") == null)
		{
			new VoidWorldGenerator().generateVoidWorld();
		}
		
		Bukkit.getServer().getWorlds().add(Bukkit.getWorld("chunksworld"));
	
		this.getCommand("chunks").setExecutor(new ChunkCreatorExecutor(this));
		this.getCommand("c").setExecutor(new ChunkCreatorExecutor(this));
		
		getServer().getPluginManager().registerEvents(this.diffInventory, this);
		getServer().getPluginManager().registerEvents(this.menuInventory, this);
		getServer().getPluginManager().registerEvents(this.settingsInventory, this);
		getServer().getPluginManager().registerEvents(new PlayerProtectionEvents(this), this);
		getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
		getServer().getPluginManager().registerEvents(new SignListeners(this), this);
		
		this.init();
	}

	public ChunkGenerator getChunkCreator()
	{
		return this.chunkGenerator;
	}
	
	public DifficultyInventory getDifficulty()
	{
		return this.diffInventory;
	}
	
	public MenuInventory getMenu()
	{
		return this.menuInventory;
	}
	
	public SettingsInventory getSettingsInventory()
	{
		return this.settingsInventory;
	}
	
	public ChunkManager getChunkManager()
	{
		return this.chunkManager;
	}
	
	public ProtectedAreaManager getProtectionManager()
	{
		return this.protectionManager;
	}
	
	private void initFiles()
	{
		this.chunkFile = new File(this.getDataFolder(), "Chunks.yml");
		this.chunk = YamlConfiguration.loadConfiguration(this.chunkFile);
		
		this.addBlock(Material.STONE, 250, 100, 0);
		this.addBlock(Material.IRON_ORE, 20, 100, 0);
		this.addBlock(Material.COAL_ORE, 20, 100, 0);
		this.addBlock(Material.LAPIS_ORE, 4, 25, 0);
		this.addBlock(Material.REDSTONE_ORE, 6, 25, 0);
		this.addBlock(Material.GOLD_ORE, 5, 20, 0);
		this.addBlock(Material.DIAMOND_ORE, 3, 12, 0);
		this.addBlock(Material.EMERALD_ORE, 1, 15, 0);
		this.addBlock(Material.SAND, 5, 100, 0);
		this.addBlock(Material.GRAVEL, 25, 100, 0);
		this.addBlock(Material.ANDESITE, 25, 100, 0);
		this.addBlock(Material.DIORITE, 25, 100, 0);
		this.addBlock(Material.GRANITE, 25, 100, 0);
		this.addBlock(Material.CHEST, 0.5f, 100, 0);
		
		this.chunk.addDefault("chunks.difficulty-multipliers.easy", 1);
		this.chunk.addDefault("chunks.difficulty-multipliers.medium", 2);
		this.chunk.addDefault("chunks.difficulty-multipliers.hard", 3);
		
		this.chunk.addDefault("chunks.default-location.X", 0);
		this.chunk.addDefault("chunks.default-location.Y", 0);
		this.chunk.addDefault("chunks.default-location.Z", 0);
		this.chunk.addDefault("chunks.default-location.WORLD", "world");
		
		makeFile(this.chunkFile, this.chunk);
		
		this.chunksFile = new File(this.getDataFolder(), "PlayerChunks.yml");
		this.chunks = YamlConfiguration.loadConfiguration(this.chunksFile);
		
		makeFile(this.chunksFile, this.chunks);
	
		this.startChestFile = new File(this.getDataFolder(), "StarterChest.yml");
		this.startChest = YamlConfiguration.loadConfiguration(this.startChestFile);
		
		this.addItemStackDefault(this.startChest, "chunks.start-chest.items", new ItemStack(Material.LAVA_BUCKET, 1));
		this.addItemStackDefault(this.startChest, "chunks.start-chest.items", new ItemStack(Material.ICE, 2));
		this.addItemStackDefault(this.startChest, "chunks.start-chest.items", new ItemStack(Material.WHEAT_SEEDS, 1));
		this.addItemStackDefault(this.startChest, "chunks.start-chest.items", new ItemStack(Material.BEETROOT_SEEDS, 1));
		this.addItemStackDefault(this.startChest, "chunks.start-chest.items", new ItemStack(Material.POTATO, 1));
		this.addItemStackDefault(this.startChest, "chunks.start-chest.items", new ItemStack(Material.CARROT, 1));
		this.addItemStackDefault(this.startChest, "chunks.start-chest.items", new ItemStack(Material.OAK_SAPLING, 2));
		this.addItemStackDefault(this.startChest, "chunks.start-chest.items", new ItemStack(Material.COW_SPAWN_EGG, 2));
		this.addItemStackDefault(this.startChest, "chunks.start-chest.items", new ItemStack(Material.CHICKEN_SPAWN_EGG, 2));
		this.addItemStackDefault(this.startChest, "chunks.start-chest.items", new ItemStack(Material.SHEEP_SPAWN_EGG, 2));
		this.addItemStackDefault(this.startChest, "chunks.start-chest.items", new ItemStack(Material.PIG_SPAWN_EGG, 2));
		this.addItemStackDefault(this.startChest, "chunks.start-chest.items", new ItemStack(Material.GLASS, 64));
		
		makeFile(this.startChestFile, this.startChest);
		
		this.lootTableFile = new File(this.getDataFolder(), "LootTable.yml");
		this.lootTable = YamlConfiguration.loadConfiguration(this.lootTableFile);
		
		this.addItemStackDefault(this.lootTable, "chunks.loot-table.items", new ItemStack(Material.DIAMOND, 1));
		this.addItemStackDefault(this.lootTable, "chunks.loot-table.items", new ItemStack(Material.EMERALD, 1));
		this.addItemStackDefault(this.lootTable, "chunks.loot-table.items", new ItemStack(Material.SAND, 16));
		this.addItemStackDefault(this.lootTable, "chunks.loot-table.items", new ItemStack(Material.ACACIA_SAPLING, 1));
		this.addItemStackDefault(this.lootTable, "chunks.loot-table.items", new ItemStack(Material.BAMBOO_SAPLING, 1));
		this.addItemStackDefault(this.lootTable, "chunks.loot-table.items", new ItemStack(Material.BIRCH_SAPLING, 1));
		this.addItemStackDefault(this.lootTable, "chunks.loot-table.items", new ItemStack(Material.DARK_OAK_SAPLING, 1));
		this.addItemStackDefault(this.lootTable, "chunks.loot-table.items", new ItemStack(Material.JUNGLE_SAPLING, 1));
		this.addItemStackDefault(this.lootTable, "chunks.loot-table.items", new ItemStack(Material.SPRUCE_SAPLING, 1));
		this.addItemStackDefault(this.lootTable, "chunks.loot-table.items", new ItemStack(Material.RED_SAND, 16));
		this.addItemStackDefault(this.lootTable, "chunks.loot-table.items", new ItemStack(Material.QUARTZ, 32));
		
		makeFile(this.lootTableFile, this.lootTable);
		
		this.settingsFile = new File(this.getDataFolder(), "Settings.yml");
		this.settings = YamlConfiguration.loadConfiguration(this.settingsFile);
		
		this.settings.addDefault("chunks.lag-prevention.block-place-delay", 10);
		
		List<String> worlds = new ArrayList<String>();
		worlds.add("world");
		worlds.add("chunksworld");
		this.settings.addDefault("chunks.commands.c.enabled-worlds", worlds);
		
		this.settings.addDefault("chunks.chunk.clear-inventory-on-create", true);
		
		this.settings.addDefault("chunks.default-size-in-chunks", 8);
		
		this.settings.addDefault("chunk-count-do-not-change", 1);
		
		makeFile(this.settingsFile, this.settings);
		
		this.uuidFile = new File(this.getDataFolder(), "UUID.yml");
		this.uuid = YamlConfiguration.loadConfiguration(this.uuidFile);
		
		makeFile(this.uuidFile, this.uuid);
	}
	
	private void init()
	{
		if (this.getChunks().isConfigurationSection("chunks"))
		{
			for (String s : this.getChunks().getConfigurationSection("chunks").getKeys(false))
			{	
				this.getChunkManager().addChunk(s);	
				this.chunkCounter ++;
			}
		}
		
		this.updateChunkCounter(0);
	}
	
	public void updateChunkCounter(int additionalChunks)
	{
		this.chunkCounter+=additionalChunks;
		metrics.addCustomChart(new Metrics.SingleLineChart("chunkcounter", new Callable<Integer>()
		{
			@Override
			public Integer call() throws Exception {
				return chunkCounter;
			}	
		}));
	}
	
	private void addBlock(Material material, float chance, int max, int min)
	{
		this.chunk.addDefault("chunks.block-chances."+material.name()+".chance", chance);
		this.chunk.addDefault("chunks.block-chances."+material.name()+".maximum-mining-level", max);
		this.chunk.addDefault("chunks.block-chances."+material.name()+".minimum-mining-level", min);
	}
	
	private void addItemStackDefault(FileConfiguration config, String path, ItemStack i)
	{
		config.addDefault(path + "." + i.getType().name() + ".amount", i.getAmount());
		config.addDefault(path + "." + i.getType().name() + ".display-name", i.getItemMeta().getLocalizedName());
	}
	
	public FileConfiguration getChunk()
	{
		return this.chunk;
	}

	public FileConfiguration getChunks()
	{
		return this.chunks;
	}
	
	public FileConfiguration getStarter()
	{
		return this.startChest;
	}
	
	public FileConfiguration getLoot()
	{
		return this.lootTable;
	}
	
	public FileConfiguration getSettings()
	{
		return this.settings;
	}
	
	public FileConfiguration getUUID()
	{
		return this.uuid;
	}
	
	public void saveChunks()
	{
		this.save(this.chunksFile, this.chunks);
	}
	
	public void saveChunk()
	{
		this.save(this.chunkFile, this.chunk);
	}
	
	public void saveUUID()
	{
		this.save(this.uuidFile, this.uuid);
	}
	
	public void saveSettings()
	{
		this.save(this.settingsFile, this.settings);
	}
	
	private void save(File file, FileConfiguration config)
	{
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void makeFile(File file, FileConfiguration config)
	{
		if (!file.exists())
		{
			try 
			{
				file.getParentFile().mkdirs();
				file.createNewFile();
			} 
			catch (IOException e) {
				System.out.print("Could not create " + file.getName() + ".yml");
			}
		}
		
		config.options().copyDefaults(true);
		
		this.save(file, config);
	}
	
	/*public ChunkGenerator getDefaultWorldGenerator(String worldname, String id)
	{
		return new VoidWorldGeneration();
	}*/
	
}
