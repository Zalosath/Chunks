package com.jordna.chunks.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.jordna.chunks.chunks.Chunk;
import com.jordna.chunks.main.Chunks;

@SuppressWarnings("deprecation")
public class SettingsInventory implements Listener
{

    private Chunks main;

    public SettingsInventory(Chunks ins)
    {
	this.main = ins;
    }

    private Inventory inventory;

    private HashMap<String, String> typing = new HashMap<String, String>();

    public void allowChatType(Player player, String mod)
    {
	if (typing.containsKey(player.getUniqueId().toString())) typing.remove(player.getUniqueId().toString());

	player.sendMessage(ChatColor.BLUE + "[CHUNKS] Please type the name of the person you would like to " + mod);
	player.closeInventory();
	typing.put(player.getUniqueId().toString(), mod);
    }

    public void initializeInventory()
    {
	this.inventory = Bukkit.createInventory(null, 27, ChatColor.BLACK + "Select an option");

	for (int i = 0; i < 27; i++)
	{
	    this.inventory.setItem(i, createStack("", new String[] { "" }, Material.GRAY_STAINED_GLASS_PANE, 1));
	}

	this.inventory.setItem(10,
		createStack(ChatColor.GREEN + "" + ChatColor.BOLD + "INVITE",
			new String[] { ChatColor.BLUE + "Invite a player to your", ChatColor.BLUE + "chunk",
				ChatColor.RED + "Chunk owners only" },
			Material.PAPER, 1));

	this.inventory.setItem(12,
		createStack(ChatColor.GREEN + "" + ChatColor.BOLD + "TRUST",
			new String[] { ChatColor.BLUE + "Trust a player onto your", ChatColor.BLUE + "chunk",
				ChatColor.RED + "Chunk owners only" },
			Material.DIAMOND_SWORD, 1));

	this.inventory.setItem(14, createStack(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "VISIT",
		new String[] { ChatColor.BLUE + "Visit another players chunk" }, Material.ARROW, 1));
	this.inventory.setItem(16,
		createStack(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "VIEW MEMBERS",
			new String[] { ChatColor.BLUE + "View all members of your", ChatColor.BLUE + "chunk" },
			Material.SHULKER_BOX, 1));

	this.inventory.setItem(19,
		createStack(ChatColor.GREEN + "" + ChatColor.BOLD + "LEAVE", new String[] {
			ChatColor.BLUE + "Leave your current chunk", ChatColor.RED + "Does not work for chunk owners" },
			Material.OAK_DOOR, 1));

	this.inventory.setItem(21,
		createStack(ChatColor.GREEN + "" + ChatColor.BOLD + "UNTRUST",
			new String[] { ChatColor.BLUE + "Untrust a player from your", ChatColor.BLUE + "chunk",
				ChatColor.RED + "Chunk owners only" },
			Material.GOLDEN_SWORD, 1));

	this.inventory.setItem(1,
		createStack(ChatColor.GREEN + "" + ChatColor.BOLD + "REVOKE MEMBERSHIP",
			new String[] { ChatColor.BLUE + "Remove a member from your", ChatColor.BLUE + "chunk",
				ChatColor.RED + "Chunk owners only" },
			Material.PAPER, 1));

	this.inventory.setItem(25,
		createStack(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "VIEW TRUSTED",
			new String[] { ChatColor.BLUE + "View all trusted on your", ChatColor.BLUE + "chunk" },
			Material.SHULKER_BOX, 1));

	this.inventory.setItem(7, createStack(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "SET ISLAND HOME",
		new String[] { ChatColor.BLUE + "Set your chunks spawn point", ChatColor.RED + "Chunk owners only" },
		Material.BEACON, 1));

	this.inventory
		.setItem(26,
			createStack(ChatColor.RED + "" + ChatColor.BOLD + "ABANDON",
				new String[] { ChatColor.RED + "Abandon and delete your chunk",
					ChatColor.RED + "Irreversible", ChatColor.RED + "Chunk owners only" },
				Material.BARRIER, 1));
    }

    public void openSure(Player player)
    {
	Inventory inv = Bukkit.createInventory(player, 27, ChatColor.BLACK + "Are you sure?");
	for (int i = 0; i < 27; i++)
	{
	    inv.setItem(i, createStack("", new String[] { "" }, Material.GRAY_STAINED_GLASS_PANE, 1));
	}

	inv.setItem(11,
		createStack(ChatColor.GREEN + "" + ChatColor.BOLD + "YES",
			new String[] { ChatColor.WHITE + "By clicking you understand",
				ChatColor.WHITE + "that your chunk will be", ChatColor.RED + "" + ChatColor.BOLD
					+ "permanently" + ChatColor.RESET + "" + ChatColor.WHITE + " removed" },
			Material.GREEN_WOOL, 1));
	
	inv.setItem(15, createStack(ChatColor.RED + "" + ChatColor.BOLD + "NO",
		new String[] { ChatColor.WHITE + "No! Don't delete my chunk!" }, Material.RED_WOOL, 1));

	player.openInventory(inv);
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

    public void openSendRequest(Chunk toJoin, Player from, Player to)
    {
	to.closeInventory();

	Inventory inv = Bukkit.createInventory(to, 27, ChatColor.BLACK + "Invite");

	for (int i = 0; i < 27; i++)
	{
	    inv.setItem(i, createStack("", new String[] { "" }, Material.GRAY_STAINED_GLASS_PANE, 1));
	}

	inv.setItem(11,
		createStack(ChatColor.GREEN + "" + ChatColor.BOLD + "YES", new String[] {
			ChatColor.BLUE + "Accept the invite and join", ChatColor.BLUE + from.getName() + "'s chunk" },
			Material.GREEN_WOOL, 1));
	
	inv.setItem(15, createStack(ChatColor.RED + "" + ChatColor.BOLD + "NO",
		new String[] { ChatColor.BLUE + "No thanks!" }, Material.RED_WOOL, 1));

	to.openInventory(inv);

    }

    @EventHandler
    public void onClick(InventoryClickEvent e)
    {
	if (e.getView().getTitle().equals(ChatColor.BLACK + "Select an option"))
	{
	    ItemStack clicked = e.getCurrentItem();
	    Player player = (Player) e.getWhoClicked();

	    if (clicked == null || !clicked.hasItemMeta() || !clicked.getItemMeta().hasDisplayName())
		e.setCancelled(true);

	    switch (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()))
	    {
	    case "INVITE":
		allowChatType(player, "invite");
		break;
	    case "REVOKE MEMBERSHIP":
		allowChatType(player, "kick");
		break;
	    case "LEAVE":
		if (main.getChunkManager().hasChunk(player))
		{
		    Chunk c = main.getChunkManager().getChunk(player.getName());
		    if (c.getOwner().equals(player.getUniqueId().toString()))
		    {
			player.sendMessage(ChatColor.RED + "[CHUNKS] You must abandon the chunk");
		    }
		    else
		    {
			main.getChunkManager().removeMember(c, player);
			player.sendMessage(ChatColor.BLUE + "[CHUNKS] You are no longer a member of the chunk");
		    }
		}
		break;
	    case "TRUST":
		allowChatType(player, "trust");
		break;
	    case "UNTRUST":
		allowChatType(player, "untrust");
		break;
	    case "VISIT":
		allowChatType(player, "visit");
		break;
	    case "VIEW MEMBERS":
		if (main.getChunkManager().hasChunk(player))
		{
		    player.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[CHUNKS] Members:");
		    for (String s : main.getChunkManager().getChunk(player.getName()).getMembers())
		    {
			if (main.getUUID().getString("players." + s) != null)
			{
			    player.sendMessage(ChatColor.BLUE + "- " + main.getUUID().getString("players." + s));
			}
			else
			{
			    player.sendMessage(ChatColor.BLUE + "- " + s + " (UNDISCOVERABLE)");
			}
		    }
		}
		else
		{
		    player.sendMessage(ChatColor.RED + "[CHUNKS] You do not have a chunk");
		}
		break;
	    case "VIEW TRUSTED":
		if (main.getChunkManager().hasChunk(player))
		{
		    player.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[CHUNKS] Trusted:");
		    for (String s : main.getChunkManager().getChunk(player.getName()).getTrusted())
		    {
			if (main.getUUID().getString("players." + s) != null)
			{
			    player.sendMessage(ChatColor.BLUE + "- " + main.getUUID().getString("players." + s));
			}
			else
			{
			    player.sendMessage(ChatColor.BLUE + s + " (UNDISCOVERABLE)");
			}
		    }
		}
		else
		{
		    player.sendMessage(ChatColor.RED + "[CHUNKS] You do not have a chunk");
		}
		break;
	    case "SET ISLAND HOME":
		if (main.getChunkManager().hasChunk(player))
		{
		    Chunk c = main.getChunkManager().getChunk(player.getName());
		    if (c.getOwner().equals(player.getUniqueId().toString()))
		    {
			this.main.getChunkManager().setHome(player, c, player.getLocation());
		    }
		    else
		    {
			player.sendMessage(ChatColor.RED + "[CHUNKS] You must be the owner of a chunk to do that");
		    }
		}
		else
		{
		    player.sendMessage(ChatColor.RED + "[CHUNKS] You do not own a chunk");
		}
		break;
	    case "ABANDON":
		if (main.getChunkManager().hasChunk(player))
		{
		    Chunk c = main.getChunkManager().getChunk(player.getName());
		    if (c.getOwner().equals(player.getUniqueId().toString()))
		    {
			player.closeInventory();
			this.openSure(player);
			e.setCancelled(true);
			return;
		    }
		    else
		    {
			player.sendMessage(ChatColor.RED + "[CHUNKS] You must be the owner of a chunk to do that");
		    }
		}
		else
		{
		    player.sendMessage(ChatColor.RED + "[CHUNKS] You do not own a chunk");
		}
		break;
	    }
	    player.closeInventory();
	    e.setCancelled(true);
	}
	else if (e.getView().getTitle().equals(ChatColor.BLACK + "Are you sure?"))
	{
	    ItemStack clicked = e.getCurrentItem();
	    Player player = (Player) e.getWhoClicked();

	    if (clicked == null || !clicked.hasItemMeta() || !clicked.getItemMeta().hasDisplayName())
		e.setCancelled(true);

	    switch (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()))
	    {
	    case "YES":
		player.sendMessage(ChatColor.RED + "[CHUNKS] Your chunk is being deleted");
		player.closeInventory();
		main.getChunkCreator()
			.deleteChunk(main.getChunkManager().getChunkFromOwner(player.getUniqueId().toString()), player);
		break;
	    case "NO":
		player.sendMessage(ChatColor.BLUE + "[CHUNKS] Your chunk has " + ChatColor.RED + "" + ChatColor.BOLD
			+ "not" + ChatColor.RESET + "" + ChatColor.BLUE + " been deleted");
	    }
	    player.closeInventory();
	    e.setCancelled(true);
	}
	else if (e.getView().getTitle().equals(ChatColor.BLACK + "Invite"))
	{
	    ItemStack clicked = e.getCurrentItem();
	    Player player = (Player) e.getWhoClicked();

	    if (clicked == null || !clicked.hasItemMeta() || !clicked.getItemMeta().hasDisplayName())
		e.setCancelled(true);

	    switch (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()))
	    {
	    case "YES":

		ItemStack itemStack = e.getClickedInventory().getItem(11);
		List<String> lore = itemStack.getItemMeta().getLore();
		String playerName = lore.get(1).split("'")[0];

		for (String s : main.getUUID().getConfigurationSection("players").getKeys(false))
		{
		    Bukkit.broadcastMessage(main.getUUID().getString("players." + s).toLowerCase());
		    if (main.getUUID().getString("players." + s).equals(ChatColor.stripColor(playerName)))
		    {
			Chunk c = main.getChunkManager().getChunkFromOwner(s);
			main.getChunkManager().addMember(c, player);
			player.sendMessage(ChatColor.BLUE + "[CHUNKS] You are now a member");
			player.teleport(c.getHome());

			if (Bukkit.getPlayer(playerName) != null)
			{
			    Bukkit.getPlayer(playerName).sendMessage(
				    ChatColor.BLUE + "[CHUNKS] " + player.getName() + " accepted the invitation");
			}

			e.setCancelled(true);
			return;
		    }
		}

		player.sendMessage(ChatColor.RED + "[CHUNKS] Something went wrong");

		break;
	    case "NO":
		player.closeInventory();
		player.sendMessage(ChatColor.BLUE + "[CHUNKS] You have declined the request");
		break;
	    }
	    player.closeInventory();
	    e.setCancelled(true);
	}
    }

    @EventHandler
    public void onType(PlayerChatEvent e)
    {
	if (this.typing.containsKey(e.getPlayer().getUniqueId().toString()))
	{

	    switch (this.typing.get(e.getPlayer().getUniqueId().toString()))
	    {
	    case "visit":
		Chunk i = main.getChunkManager().getChunk(e.getMessage());
		if (i == null)
		{
		    e.getPlayer().sendMessage(ChatColor.RED + "[CHUNKS] That player could not be found");
		}
		else
		{
		    e.getPlayer().teleport(i.getHome());
		}
		break;
	    case "invite":
		if (main.getChunkManager().hasChunk(e.getPlayer()))
		{
		    Player target = Bukkit.getPlayer(e.getMessage());
		    if (target == null)
		    {
			e.getPlayer().sendMessage(ChatColor.RED + "[CHUNKS] " + e.getMessage() + " could not be found");
		    }
		    else
		    {
			if (main.getChunkManager().getChunk(e.getPlayer().getName()).getOwner()
				.equals(e.getPlayer().getUniqueId().toString()))
			{
			    Chunk chunk = main.getChunkManager()
				    .getChunkFromOwner(e.getPlayer().getUniqueId().toString());

			    if (!main.getChunkManager().hasChunk(target))
			    {
				if (chunk.getMembers().contains(target.getUniqueId().toString()))
				{
				    e.getPlayer().sendMessage(
					    ChatColor.RED + "[CHUNKS] That player is already a member of your chunk");
				}
				else
				{
				    e.getPlayer().sendMessage(ChatColor.BLUE + "[CHUNKS] " + target.getName()
					    + " has been invited to your chunk");
				    this.openSendRequest(chunk, e.getPlayer(), target);
				}
			    }
			    else
			    {
				if (chunk.getMembers().contains(target.getUniqueId().toString()))
				    e.getPlayer().sendMessage(ChatColor.RED + "[CHUNKS] " + target.getName()
					    + " is already a member of your chunk");
				else e.getPlayer().sendMessage(ChatColor.RED + "[CHUNKS] " + target.getName()
					+ " already has a chunk, they must abandon or leave it first");
			    }
			}
			else
			{
			    e.getPlayer().sendMessage(ChatColor.RED + "[CHUNKS] Only the chunk owner may add members");
			}
		    }
		}
		else
		{
		    e.getPlayer().sendMessage(ChatColor.RED + "[CHUNKS] You do not have a chunk");
		}
		break;
	    case "kick":
		if (main.getChunkManager().hasChunk(e.getPlayer()))
		{
		    Player target = Bukkit.getPlayer(e.getMessage());
		    if (target == null)
		    {
			e.getPlayer().sendMessage(ChatColor.RED + "[CHUNKS] " + e.getMessage() + " could not be found");
		    }
		    else
		    {
			Chunk c = main.getChunkManager().getChunk(e.getPlayer().getName());
			if (c.getOwner().equals(e.getPlayer().getUniqueId().toString()))
			{
			    if (!target.getUniqueId().toString().equalsIgnoreCase(c.getOwner()))
			    {
				if (c.getMembers().contains(target.getUniqueId().toString()))
				{
				    main.getChunkManager().removeMember(c, target);
				    e.getPlayer().sendMessage(ChatColor.BLUE + "[CHUNKS] " + target.getName()
					    + " has been kicked from your chunk");
				}
				else
				{
				    e.getPlayer().sendMessage(ChatColor.RED + "[CHUNKS] " + target.getName()
					    + " is not a member of your chunk");
				}
			    }
			    else
			    {
				e.getPlayer().sendMessage(
					ChatColor.RED + "[CHUNKS] You cannot be kicked from your own chunk");
			    }
			}
			else
			{
			    e.getPlayer().sendMessage(ChatColor.RED + "[CHUNKS] Only the chunk owner may do that");
			}
		    }
		}
		break;
	    case "trust":
		if (main.getChunkManager().hasChunk(e.getPlayer()))
		{
		    Player target = Bukkit.getPlayer(e.getMessage());
		    if (target == null)
		    {
			e.getPlayer().sendMessage(ChatColor.RED + "[CHUNKS] " + e.getMessage() + " could not be found");
		    }
		    else
		    {
			if (main.getChunkManager().getChunk(e.getPlayer().getName()).getOwner()
				.equals(e.getPlayer().getUniqueId().toString()))
			{
			    Chunk chunk = main.getChunkManager()
				    .getChunkFromOwner(e.getPlayer().getUniqueId().toString());
			    if (chunk.getTrusted().contains(target.getUniqueId().toString()))
			    {
				e.getPlayer().sendMessage(
					ChatColor.RED + "[CHUNKS] " + target.getName() + " is already trusted");
			    }
			    else
			    {
				main.getChunkManager().addTrusted(chunk, target);
				e.getPlayer().sendMessage(
					ChatColor.BLUE + "[CHUNKS] " + target.getName() + " has been trusted");
			    }
			}
			else
			{
			    e.getPlayer().sendMessage(
				    ChatColor.RED + "[CHUNKS] Only the chunk owner may trust other players");
			}
		    }
		}
		else
		{
		    e.getPlayer().sendMessage(ChatColor.RED + "[CHUNKS] You do not have a chunk");
		}
		break;
	    case "untrust":
		if (main.getChunkManager().hasChunk(e.getPlayer()))
		{
		    Player target = Bukkit.getPlayer(e.getMessage());
		    if (target == null)
		    {
			e.getPlayer().sendMessage(ChatColor.RED + "[CHUNKS] " + e.getMessage() + " could not be found");
		    }
		    else
		    {
			Chunk c = main.getChunkManager().getChunk(e.getPlayer().getName());
			if (c.getOwner().equals(e.getPlayer().getUniqueId().toString()))
			{
			    if (!target.getUniqueId().toString().equalsIgnoreCase(c.getOwner()))
			    {
				if (c.getTrusted().contains(target.getUniqueId().toString()))
				{
				    main.getChunkManager().removeTrusted(c, target);
				    e.getPlayer().sendMessage(
					    ChatColor.RED + "[CHUNKS] " + target.getName() + " has been untrusted");
				}
				else
				{
				    e.getPlayer().sendMessage(
					    ChatColor.RED + "[CHUNKS] " + target.getName() + " is not trusted");
				}
			    }
			    else
			    {
				e.getPlayer().sendMessage(
					ChatColor.RED + "[CHUNKS] You cannot untrust yourself from your own chunk");
			    }
			}
			else
			{
			    e.getPlayer().sendMessage(ChatColor.RED + "[CHUNKS] Only the chunk owner may do that");
			}
		    }
		}
	    }

	    this.typing.remove(e.getPlayer().getUniqueId().toString());
	    e.setCancelled(true);
	}
    }

}
