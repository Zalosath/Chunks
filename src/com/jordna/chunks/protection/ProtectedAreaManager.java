package com.jordna.chunks.protection;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.jordna.chunks.chunks.Chunk;
import com.jordna.chunks.main.Chunks;

public class ProtectedAreaManager 
{

	private Chunks main;
	public ProtectedAreaManager(Chunks ins)
	{
		this.main = ins;
	}
	
	public boolean canDoHere(Player player, Location loc)
	{
		if (!player.getWorld().getName().equals("chunksworld"))
			return true;
	
		if (player.hasPermission("chunks.admin.bypass"))
			return true;
		
		Chunk chunkIn = main.getChunkManager().getChunkIn(loc);
		
		if (chunkIn != null)
		{
			if (!chunkIn.isTrusted(player) && !chunkIn.isMember(player))
			{
				player.sendMessage(ChatColor.RED + "[CHUNKS] You cannot do that here");
				return false;
			}
			else
			{
				return true;
			}
		}
		
		return false;
	}
	
}
