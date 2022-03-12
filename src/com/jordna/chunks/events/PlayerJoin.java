package com.jordna.chunks.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.jordna.chunks.main.Chunks;

public class PlayerJoin implements Listener
{

	private Chunks main;
	public PlayerJoin (Chunks instance)
	{
		this.main = instance;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		if (!main.getUUID().isConfigurationSection("players." + e.getPlayer().getUniqueId().toString()) 
				|| !main.getUUID().get("players." + e.getPlayer().getUniqueId().toString()).equals(e.getPlayer().getName()))
		{
			main.getUUID().set("players." + e.getPlayer().getUniqueId().toString(), e.getPlayer().getName());
			main.saveUUID();
		}
	}
	
}
