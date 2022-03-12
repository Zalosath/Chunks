package com.jordna.chunks.events;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.jordna.chunks.main.Chunks;

import net.md_5.bungee.api.ChatColor;

public class SignListeners implements Listener
{

	private Chunks chunks;
	public SignListeners(Chunks chunks)
	{
		this.chunks = chunks;
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent e)
	{
		if (e.getLine(0).equalsIgnoreCase("[Chunks]"))
		{
			if (e.getPlayer().isOp())
			{
				if (e.getLine(1).equalsIgnoreCase("Open"))
				{
					e.setLine(0, ChatColor.BLACK + "[Chunks]");
					e.setLine(1, ChatColor.BLUE + "Open");
					e.getPlayer().sendMessage(ChatColor.BLUE + "[Chunks] Sign created");
				}
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e)
	{
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if (e.hasBlock())
			{
				if (e.getClickedBlock().getType() == Material.OAK_SIGN || e.getClickedBlock().getType() == Material.OAK_WALL_SIGN)
				{
					Sign s = (Sign) e.getClickedBlock().getState();
					if (ChatColor.stripColor(s.getLine(0)).equalsIgnoreCase("[Chunks]"))
					{
						if (ChatColor.stripColor(s.getLine(1)).equalsIgnoreCase("Open"))
						{
							if (e.getPlayer().hasPermission("chunks.commands.c.use"))
							{
								chunks.getMenu().openInventory(e.getPlayer());
							}
							else
							{
								e.getPlayer().sendMessage(ChatColor.RED + "[Chunks] You don't have permission to do that");
							}
						}
					}
				}
			}
		}
	}
	
}
