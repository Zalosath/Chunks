package com.jordna.chunks.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.jordna.chunks.main.Chunks;

public class MenuInventory implements Listener
{

    private Chunks main;

    public MenuInventory(Chunks ins)
    {
	this.main = ins;
    }

    private Inventory inventory;

    public void initializeInventory()
    {
	this.inventory = Bukkit.createInventory(null, 27, ChatColor.BLACK + "Options");

	for (int i = 0; i < 27; i++)
	{
	    this.inventory.setItem(i,
		    createStack("             ", new String[] { "" }, Material.GRAY_STAINED_GLASS_PANE, 1));
	}

	this.inventory.setItem(11,
		createStack(ChatColor.GREEN + "" + ChatColor.BOLD + "CREATE CHUNK", new String[] {
			ChatColor.BLUE + "Create an chunk, click me", ChatColor.BLUE + "to select a difficulty" },
			Material.GRASS_BLOCK, 1));
	this.inventory.setItem(13,
		createStack(ChatColor.GOLD + "" + ChatColor.BOLD + "HOME",
			new String[] { ChatColor.BLUE + "Teleport back to your chunk", ChatColor.BLUE + "Effective!" },
			Material.IRON_DOOR, 1));
	this.inventory.setItem(15,
		createStack(ChatColor.BLUE + "" + ChatColor.BOLD + "CHUNK SETTINGS", new String[] {
			ChatColor.BLUE + "Invite players, trust players", ChatColor.BLUE + "and general settings!" },
			Material.REPEATER, 1));

    }

    private ItemStack createStack(String name, String[] lore, Material material, int stackSize)
    {
	ItemStack i = new ItemStack(material, stackSize);
	ItemMeta im = i.getItemMeta();
	im.setDisplayName(name);
	List<String> l = new ArrayList<String>();

	for (String s : lore)
	{
	    l.add(s);
	}

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
	if (e.getView().getTitle().equals(ChatColor.BLACK + "Options"))
	{
	    ItemStack clicked = e.getCurrentItem();
	    Player player = (Player) e.getWhoClicked();

	    if (clicked == null || !clicked.hasItemMeta() || !clicked.getItemMeta().hasDisplayName())
		e.setCancelled(true);

	    switch (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()))
	    {
	    case "CREATE CHUNK":
		this.main.getDifficulty().openInventory(player);
		break;
	    case "HOME":
		Location loc = main.getChunkManager().getHome(player);
		if (loc == null)
		{
		    player.closeInventory();
		    player.sendMessage(ChatColor.RED + "[CHUNKS] You do not have a chunk");
		    e.setCancelled(true);
		}
		else
		{
		    loc.setYaw(0f);
		    loc.setPitch(0f);

		    player.teleport(loc);
		}
		break;
	    case "CHUNK SETTINGS":
		player.closeInventory();
		main.getSettingsInventory().openInventory(player);
		break;
	    }
	    e.setCancelled(true);
	}

    }

}
