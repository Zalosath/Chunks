package com.jordna.chunks.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.jordna.chunks.main.Chunks;

public class DifficultyInventory implements Listener
{

	private Chunks main;
	public DifficultyInventory(Chunks ins)
	{
		this.main = ins;
	}
	
	private Inventory inventory;
	
	public void initializeInventory()
	{
		this.inventory = Bukkit.createInventory(null, 27, ChatColor.BLACK + "Select a difficulty");
		
		for(int i = 0; i < 27; i++)
		{
			this.inventory.setItem(i, createStack("", new String[] {""}, Material.GRAY_STAINED_GLASS_PANE, 1));
		}
		
		this.inventory.setItem(11, createStack(ChatColor.GREEN + "" + ChatColor.BOLD + "EASY", new String[] {ChatColor.BLUE + "Easy mode, the most resources", ChatColor.BLUE + "and the easiest start", ChatColor.BLUE + "Difficulty Multiplier: " + ChatColor.GRAY + main.getChunk().getInt("chunks.difficulty-multipliers.easy")}, Material.GREEN_WOOL, 1));
		this.inventory.setItem(13, createStack(ChatColor.GOLD + "" + ChatColor.BOLD + "MEDIUM", new String[] {ChatColor.BLUE + "Medium mode, less resources", ChatColor.BLUE + "and a more difficult start", ChatColor.BLUE + "Difficulty Multiplier: " + ChatColor.GRAY + main.getChunk().getInt("chunks.difficulty-multipliers.medium")}, Material.ORANGE_WOOL, 1));
		this.inventory.setItem(15, createStack(ChatColor.RED + "" + ChatColor.BOLD + "HARD", new String[] {ChatColor.BLUE + "Hard mode, next to no resources", ChatColor.BLUE + "and the hardest start", ChatColor.BLUE + "Difficulty Multiplier: " + ChatColor.GRAY + main.getChunk().getInt("chunks.difficulty-multipliers.hard")}, Material.RED_WOOL, 1));
	}
	
	private ItemStack createStack(String name, String[] lore, Material material, int stackSize)
	{
		ItemStack i = new ItemStack(material, stackSize);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(name);
		List<String> l = new ArrayList<String>();
		
		for(String s : lore) {l.add(s);}
		
		im.setLore(l);
		i.setItemMeta(im);
		
		return i;
	}
	
	public void openInventory(Player player)
	{
		player.openInventory(inventory);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e)
	{
		if (e.getView().getTitle().equals(ChatColor.BLACK + "Select a difficulty"))
		{
			ItemStack clicked = e.getCurrentItem();
			Player player = (Player)e.getWhoClicked();
			
			World chunkWorld = Bukkit.getWorld("chunksworld");
			
			if (clicked == null || !clicked.hasItemMeta() || !clicked.getItemMeta().hasDisplayName())
				e.setCancelled(true);
			
			switch(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()))
			{
			case "EASY":
				if (this.main.getChunkCreator().generateChunk(player, chunkWorld, main.getChunk().getInt("chunks.difficulty-multipliers.easy")))
				{
					player.sendMessage(ChatColor.BLUE + "[CHUNKS] Easy level chunk is being created.");
				}
				break;
			case "MEDIUM":
				if (this.main.getChunkCreator().generateChunk(player, chunkWorld, main.getChunk().getInt("chunks.difficulty-multipliers.medium")))
				{
					player.sendMessage(ChatColor.BLUE + "[CHUNKS] Medium level chunk is being created.");
				}
				break;
			case "HARD":
				if (this.main.getChunkCreator().generateChunk(player, chunkWorld, main.getChunk().getInt("chunks.difficulty-multipliers.hard")))
				{
					player.sendMessage(ChatColor.BLUE + "[CHUNKS] Hard level chunk is being created.");
				}
				break;
			}
			player.closeInventory();
			e.setCancelled(true);
		}
	}
	
}
